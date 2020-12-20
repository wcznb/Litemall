package cn.edu.xmu.user.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.bloom.BloomFilterHelper;
import cn.edu.xmu.ooad.util.bloom.RedisBloomFilter;
import cn.edu.xmu.user.mapper.CustomerPoMapper;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPo;
import cn.edu.xmu.user.model.po.CustomerPoExample;
import cn.edu.xmu.user.model.vo.CustomerSetVo;
import cn.edu.xmu.user.model.vo.NewUserVo;
import cn.edu.xmu.user.model.vo.CustomerRetVo;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * 新增用户Dao
 */
@Repository
public class NewUserDao implements InitializingBean {
    private  static  final Logger logger = LoggerFactory.getLogger(NewUserDao.class);

    @Autowired
    CustomerPoMapper customerPoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    RedisBloomFilter bloomFilter;

    String[] fieldName;
    final String suffixName="BloomFilter";

    /**
     * 通过该参数选择是否清空布隆过滤器
     */
    private boolean reinitialize=true;


    /**
     * 初始化布隆过滤器
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        BloomFilterHelper bloomFilterHelper=new BloomFilterHelper<>(Funnels.stringFunnel(Charsets.UTF_8),1000,0.02);
        fieldName=new String[]{"email","mobile","userName"};
        bloomFilter=new RedisBloomFilter(redisTemplate,bloomFilterHelper);
        if(reinitialize){
            for(int i=0;i<fieldName.length;i++){
                redisTemplate.delete(fieldName[i]+suffixName);
            }
        }

    }

    /**
     *
     * @param po
     * @return ReturnObject 错误返回对象
     */
    public ReturnObject checkBloomFilter(CustomerPo po){
        if(bloomFilter.includeByBloomFilter("email"+suffixName,po.getEmail())){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        if(bloomFilter.includeByBloomFilter("mobile"+suffixName,po.getMobile())){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        if(bloomFilter.includeByBloomFilter("userName"+suffixName,po.getUserName())){
            return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
        }
        return null;

    }

    /**
     * 由属性名及属性值设置相应布隆过滤器
     * @param name 属性名
     * @param po po对象
     */
    public void setBloomFilterByName(String name,CustomerPo po) {
        try {
            Field field = CustomerPo.class.getDeclaredField(name);
            Method method=po.getClass().getMethod("get"+name.substring(0,1).toUpperCase()+name.substring(1));
            logger.debug("add value "+method.invoke(po)+" to "+field.getName()+suffixName);
            bloomFilter.addByBloomFilter(field.getName()+suffixName,method.invoke(po));
        }
        catch (Exception ex){
            logger.error("Exception happened:"+ex.getMessage());
        }
    }

    /**
     * 由vo创建newUser检查重复后插入
     * @param vo vo对象
     * @return ReturnObject
     */
    public ReturnObject createNewUserByVo(NewUserVo vo){
        CustomerPo customerPo=new CustomerPo();
        ReturnObject returnObject = null;
        customerPo.setEmail(vo.getEmail());
        customerPo.setMobile(vo.getMobile());
        customerPo.setUserName(vo.getUserName());

        if(isEmailExist(customerPo.getEmail())){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        if(isMobileExist(customerPo.getMobile())){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        if(isUserNameExist(customerPo.getUserName())){
            return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
        }


        customerPo.setPassword(vo.getPassword());
        customerPo.setRealName(vo.getRealName());
        customerPo.setPoint(0);
        customerPo.setGender(vo.getGender());
        LocalDateTime localDateTime = LocalDateTime.now();
        customerPo.setGmtCreate(localDateTime);
        customerPo.setGmtModified(localDateTime);
        customerPo.setState((byte)4);


        LocalDate localDate = LocalDate.parse(vo.getBirthday(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        customerPo.setBirthday(localDate);
        customerPo.setState(Customer.State.NORM.getCode().byteValue());

        try{
            int ret = customerPoMapper.insert(customerPo);
            if(ret!=0) {
                CustomerPoExample example = new CustomerPoExample();
                CustomerPoExample.Criteria criteria = example.createCriteria();
                criteria.andUserNameEqualTo(customerPo.getUserName());
                customerPo = customerPoMapper.selectByExample(example).get(0);
                returnObject=new ReturnObject<>(new CustomerRetVo(customerPo));
            }
            logger.debug("success trying to insert newUser");
        }

        catch (DuplicateKeyException e){
            logger.debug("failed trying to insert newUser");
            //e.printStackTrace();
            String info=e.getMessage();
            if(info.contains("user_name_uindex")){
                setBloomFilterByName("userName",customerPo);
                return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
            }
            if(info.contains("email_uindex")){
                setBloomFilterByName("email",customerPo);
                return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
            }
            if(info.contains("mobile_uindex")){
                setBloomFilterByName("mobile",customerPo);
                return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
            }

        }
        catch (Exception e){
            logger.error("Internal error Happened:"+e.getMessage());
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
        return returnObject;
    }
    /**
     * 检查用户名重复
     * @param userName 需要检查的用户名
     * @return boolean
     */
    public boolean isUserNameExist(String userName){
        logger.debug("is checking userName in user table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<CustomerPo> userPos=customerPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }

    /**
     * 检查邮箱重复
     * @param email
     * @return boolean
     */
    public boolean isEmailExist(String email){
        logger.debug("is checking email in user table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<CustomerPo> userPos=customerPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }

    /**
     * 检查电话重复
     * @param mobile 电话号码
     * @return boolean
     * createdBy LiangJi@3229
     */
    public boolean isMobileExist(String mobile){
        logger.debug("is checking mobile in user table");
        CustomerPoExample example=new CustomerPoExample();
        CustomerPoExample.Criteria criteria=example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<CustomerPo> userPos=customerPoMapper.selectByExample(example);
        return !userPos.isEmpty();
    }


}

