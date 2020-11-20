package cn.edu.xmu.user.dao;

import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.user.UserserviceApplication;
import cn.edu.xmu.user.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(classes = UserserviceApplication.class)
@Transactional
public class UserDaoTest1 {
    @Autowired
    UserDao userDao;

    @Test
    public void test1(){
        System.out.println(userDao.getUsersMix(null,null,null,1,2).getData());
    }

    /**
     * 主要测试布隆过滤器
     */

}
