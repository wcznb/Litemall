package cn.edu.xmu.user.dao;

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
import java.util.List;

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
}
