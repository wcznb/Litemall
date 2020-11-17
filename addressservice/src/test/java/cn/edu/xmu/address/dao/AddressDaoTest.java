package cn.edu.xmu.address.dao;

import cn.edu.xmu.address.AddressApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = AddressApplication.class)
@Transactional
public class AddressDaoTest {
    @Autowired
    AddressDao addressDao;

    @Test
    public void test1(){
        addressDao.test();
    }
}
