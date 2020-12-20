package cn.edu.xmu.user.service.impl;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.dao.NewUserDao;
import cn.edu.xmu.user.dao.UserDao;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.vo.CustomerSetVo;
import cn.edu.xmu.user.model.vo.NewUserVo;
import cn.edu.xmu.user.service.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Value("${userservice.login.jwtExpire}")
    private Integer jwtExpireTime;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${userservice.login.multiply}")
    private Boolean canMultiplyLogin;

    /**
     * 分布式锁的过期时间（秒）
     */
    @Value("${userservice.lockerExpireTime}")
    private long lockerExpireTime;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    NewUserDao newUserDao;

    @Autowired
    UserDao userDao;

    /**
     * @param vo 注册的vo对象
     * @return ReturnObject
     * @author LiangJi3229
     */
    @Override
    @Transactional
    public ReturnObject register(NewUserVo vo) {
        return newUserDao.createNewUserByVo(vo);
    }

    @Override
    @Transactional
    public ReturnObject<String> login(String userName, String password, String ipAddr)
    {
        ReturnObject retObj = userDao.getUserByName(userName);
        if (retObj.getCode() != ResponseCode.OK){
            return retObj;
        }

        Customer user = (Customer) retObj.getData();
        if(user == null || !password.equals(user.getPassword())){
            retObj = new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
            return retObj;
        }
        if (user.getState() != Customer.State.NORM){
            retObj = new ReturnObject<>(ResponseCode.AUTH_USER_FORBIDDEN);
            return retObj;
        }

        String key = "up_" + user.getId();
        if(redisTemplate.hasKey(key) && !canMultiplyLogin){
//            logger.debug("login: multiply  login key ="+key);
            // 用户重复登录处理
            Set<Serializable> set = redisTemplate.opsForSet().members(key);
            redisTemplate.delete(key);

            /* 将旧JWT加入需要踢出的集合 */
            String jwt = null;
            for (Serializable str : set) {
                /* 找出JWT */
                if((str.toString()).length() > 8){
                    jwt =  str.toString();
                    break;
                }
            }
            this.banJwt(jwt);
        }


        //创建新的token
        JwtHelper jwtHelper = new JwtHelper();
        Random random = new Random();
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()+random.nextInt();
        String jwt = jwtHelper.createToken(user.getId(),-2L, jwtExpireTime);
        redisTemplate.opsForSet().add(key, jwt);
        logger.debug("login: newJwt = "+ jwt);
        retObj = new ReturnObject<>(jwt);

        return retObj;
    }

    /**
     * 禁止持有特定令牌的用户登录
     * @param jwt JWT令牌
     */
    @Override
    public void banJwt(String jwt){
        String[] banSetName = {"BanJwt_0", "BanJwt_1"};
        long bannIndex = 0;
        if (!redisTemplate.hasKey("banIndex")){
            redisTemplate.opsForValue().set("banIndex", Long.valueOf(0));
        } else {
            logger.debug("banJwt: banIndex = " +redisTemplate.opsForValue().get("banIndex"));
            bannIndex = Long.parseLong(redisTemplate.opsForValue().get("banIndex").toString());
        }
        logger.debug("banJwt: banIndex = " + bannIndex);
        String currentSetName = banSetName[(int) (bannIndex % banSetName.length)];
        logger.debug("banJwt: currentSetName = " + currentSetName);
        if(!redisTemplate.hasKey(currentSetName)) {
            // 新建
            logger.debug("banJwt: create ban set" + currentSetName);
            redisTemplate.opsForSet().add(currentSetName, jwt);
            redisTemplate.expire(currentSetName,jwtExpireTime * 2, TimeUnit.SECONDS);
        }else{
            //准备向其中添加元素
            if(redisTemplate.getExpire(currentSetName, TimeUnit.SECONDS) > jwtExpireTime) {
                // 有效期还长，直接加入
                logger.debug("banJwt: add to exist ban set" + currentSetName);
                redisTemplate.opsForSet().add(currentSetName, jwt);
            } else {
                // 有效期不够JWT的过期时间，准备用第二集合，让第一个集合自然过期
                // 分步式加锁
                logger.debug("banJwt: switch to next ban set" + currentSetName);
                long newBanIndex = bannIndex;
                while (newBanIndex == bannIndex &&
                        !redisTemplate.opsForValue().setIfAbsent("banIndexLocker","nouse", lockerExpireTime, TimeUnit.SECONDS)){
                    //如果BanIndex没被其他线程改变，且锁获取不到
                    try {
                        Thread.sleep(10);
                        //重新获得新的BanIndex
                        newBanIndex = (Long) redisTemplate.opsForValue().get("banIndex");
                    }catch (InterruptedException e){
                        logger.error("banJwt: 锁等待被打断");
                    }
                    catch (IllegalArgumentException e){

                    }
                }
                if (newBanIndex == bannIndex) {
                    //切换ban set
                    bannIndex = redisTemplate.opsForValue().increment("banIndex");
                }else{
                    //已经被其他线程改变
                    bannIndex = newBanIndex;
                }

                currentSetName = banSetName[(int) (bannIndex % banSetName.length)];
                //启用之前，不管有没有，先删除一下，应该是没有，保险起见
                redisTemplate.delete(currentSetName);
                logger.debug("banJwt: next ban set =" + currentSetName);
                redisTemplate.opsForSet().add(currentSetName, jwt);
                redisTemplate.expire(currentSetName,jwtExpireTime * 2,TimeUnit.SECONDS);
                // 解锁
                redisTemplate.delete("banIndexLocker");
            }
        }
    }

    /**
     * 用户登出
     * @param userId 用户id
     */
    @Override
    public ReturnObject Logout(Long userId)
    {
        redisTemplate.delete("up_" + userId);
        return new ReturnObject<>(true);
    }

    @Override
    public ReturnObject<PageInfo<VoObject>> getallusers(String userName, String email, String mobile, Integer page, Integer pageSize){


        ReturnObject<PageInfo<VoObject>> ret = userDao.getUsersMix(userName, email, mobile, page, pageSize);
        return ret;
    }

    /**
     * 平台管理员封禁买家
     * @param did
     * @param userId 用户id
     */
    @Override
    @Transactional
    public ReturnObject<Object> banCustomer(Long did,Long userId){
        if(did == Long.valueOf(0)){
            CustomerPo customerPo = new CustomerPo();
            customerPo.setId(userId);
            customerPo.setState(Customer.State.FORBID.getCode().byteValue());//设置状态为封禁
            customerPo.setGmtModified(LocalDateTime.now());
            ReturnObject<Object> retObj = userDao.modifyCustomerByPo(customerPo);
            if (retObj.getCode() != ResponseCode.OK){
                return retObj;
            }
            return new ReturnObject<>(true);
        } else
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);//无权限

    }

    /**
     * 平台管理员解禁买家
     * @param userId 用户id
     */
    @Override
    @Transactional
    public ReturnObject<Object> releaseCustomer(Long did,Long userId){
        if(did == Long.valueOf(0)){
            CustomerPo customerPo = new CustomerPo();
            customerPo.setId(userId);
            customerPo.setState(Customer.State.NORM.getCode().byteValue());//设置状态为正常
            customerPo.setGmtModified(LocalDateTime.now());
            ReturnObject<Object> retObj = userDao.modifyCustomerByPo(customerPo);
            if (retObj.getCode() != ResponseCode.OK){
                return retObj;
            }
            return new ReturnObject<>();
        } else
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID);//无权限

    }

    @Override
    @Transactional
    public ReturnObject<Object> getCustomer(Long id) {

        String key = "up_" + id;
//        if(!redisTemplate.hasKey(key)){
//            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
//        }
        ReturnObject<Object> retObj = userDao.getCustomerById(id);
        return retObj;
    }

    @Override
    public ReturnObject<Object> modifyCustomer(Long id, CustomerSetVo vo) {
        CustomerPo customerPo = new CustomerPo();
        customerPo.setId(id);
        customerPo.setBirthday(vo.getBirthday().toLocalDate());
        customerPo.setGender(vo.getGender());
        customerPo.setRealName(vo.getRealName());

        ReturnObject<Object> retObj = userDao.modifyCustomerByPo(customerPo);
        ReturnObject<Object> retCustomer;
        if (retObj.getCode().equals(ResponseCode.OK)) {
            retCustomer = new ReturnObject<>(retObj.getData());
        } else {
            retCustomer = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
        }
        return retCustomer;
    }

    /**
     * ID获取用户信息
     * @param id
     * @return 用户
     */
    public ReturnObject<VoObject> findUserById(Long id) {
        ReturnObject<VoObject> returnObject = null;
        CustomerPo customerPo = userDao.findUserById(id);

        if( customerPo!= null) {
            logger.debug("findUserById : " + returnObject);
            returnObject = new ReturnObject<>(new Customer(customerPo));
        } else {
            logger.debug("findUserById: Not Found");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);//返回错误码 RESOURCE_ID_NOTEXIST(504,"操作的资源id不存在"),
        }
        return returnObject;
    }
}
