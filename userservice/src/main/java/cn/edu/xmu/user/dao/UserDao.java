package cn.edu.xmu.user.dao;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.encript.AES;
import cn.edu.xmu.user.mapper.CustomerPoMapper;
import cn.edu.xmu.user.model.bo.Customer;
import cn.edu.xmu.user.model.po.CustomerPoExample;
import cn.edu.xmu.user.model.vo.NewUserVo;
import cn.edu.xmu.user.model.po.CustomerPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Repository
public class UserDao {
    @Autowired
    CustomerPoMapper customerPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);



    /**
     * 由用户名获得用户
     *
     * @param userName
     * @return
     */
    public ReturnObject<Customer> getUserByName(String userName) {
        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<CustomerPo> users = null;
        try {
            users = customerPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            StringBuilder message = new StringBuilder().append("getUserByName: ").append(e.getMessage());
            logger.error(message.toString());
        }
        if (null == users || users.isEmpty()) {
            return new ReturnObject<>();
        } else {
            Customer user = new Customer(users.get(0));
            return new ReturnObject<>(user);
        }
    }

    /**
     * 使用api：平台管理员获取所有用户列表
     *
     * @param userName 用户名
     * @param email 用户电子邮箱
     * @param mobile 用户电话
     * @param page 页数
     * @param pageSize 每页大小
     * @return 列表用户
     */
//    String userName, String email,String mobile, Integer page, Integer pageSize
    public ReturnObject<PageInfo<VoObject>> getUsersMix(String userName, String email,String mobile, Integer page, Integer pageSize){

        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        if(userName!=null && !userName.equals("")) criteria.andUserNameEqualTo(userName);
        if(email!=null && !email.equals("")) criteria.andEmailEqualTo(email);
        if(mobile!=null && !mobile.equals("")) criteria.andMobileEqualTo(mobile);

//        分页查询
        PageHelper.startPage(page, pageSize);
        List<CustomerPo> customerPos = null;

        try {
            //按照条件进行查询
            customerPos = customerPoMapper.selectByExample(example);
        }catch (DataAccessException e){
//            logger.error("selectAllRole: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }

        List<VoObject> ret = new ArrayList<>(customerPos.size());
        for (CustomerPo po : customerPos) {
            Customer customer = new Customer(po);
            ret.add(customer);
        }
        PageInfo<VoObject> rolePage = PageInfo.of(ret);
        return new ReturnObject<>(rolePage);
    }

    /**
     * 通过用户id更新用户信息
     *
     * @param customerPo
     * @return
     */
    public ReturnObject<Object> modifyCustomerByPo(CustomerPo customerPo){
        try{
            int ret = customerPoMapper.updateByPrimaryKeySelective(customerPo);
            Long id = customerPo.getId();
            //检查更新是否成功
            if(ret == 0){
                logger.info("买家不存在或已被删除：id = " + id);
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("买家 id = " + id + " 的资料已更新");
                return new ReturnObject<>();
            }
        }
        catch (Exception e){
            logger.error("Internal error Happened:"+e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    /**
     * 由用户id获得用户
     * @param id
     * @return
     */
    public ReturnObject<Object> getCustomerById(Long id) {
        CustomerPo customerPo=customerPoMapper.selectByPrimaryKey(id);
        if (customerPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Customer customer = new Customer(customerPo);
        return new ReturnObject<>(customer);
    }

    /**
     * ID获取用户信息
     * @author ww
     * @param Id
     * @return 用户
     */
    public CustomerPo findUserById(Long Id) {

        //mybatis的逆向工程中会生成实例及实例对应的example，example用于添加条件，相当where后面的部分
        //    xxxExample example = new xxxExample();
        //    Criteria criteria = new Example().createCriteria();

        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(Id);                               //    criteria.andXxxEqualTo(value)    添加xxx字段等于value条件

        logger.debug("findUserById: Id =" + Id);

        CustomerPo customerPo = customerPoMapper.selectByPrimaryKey(Id);//根据其主键查询，则可以用selectByPrimaryKey（）

        return customerPo;
    }
}
