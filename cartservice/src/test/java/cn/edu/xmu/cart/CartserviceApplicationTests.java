package cn.edu.xmu.cart;

import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = CartserviceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CartserviceApplicationTests {
    @Autowired
    private MockMvc mvc;

    private JwtHelper jwtHelper = new JwtHelper();

    @Test
    void contextLoads() {
    }

}
