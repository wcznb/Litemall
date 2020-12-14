package cn.edu.xmu.advertisement;

import cn.edu.xmu.ooad.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
//class AdvertisementserviceApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
//
//}
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AdvertisementserviceApplicationTests{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
