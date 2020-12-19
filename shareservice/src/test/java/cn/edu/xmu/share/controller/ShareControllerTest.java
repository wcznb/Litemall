package cn.edu.xmu.share.controller;

import cn.edu.xmu.share.ShareserviceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import cn.edu.xmu.share.util.RestGetObject;

@SpringBootTest(classes = ShareserviceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ShareControllerTest {
    RestGetObject restGetObject = new RestGetObject();
    @Test
    public void getshareactivies(){
        System.out.println("asdasdasd");
        System.out.println("asdasdasdasdasdasdssss");
//        System.out.println(restGetObject.getfromurl("http://127.0.0.1:8080/shareactivities"));
    }
}


