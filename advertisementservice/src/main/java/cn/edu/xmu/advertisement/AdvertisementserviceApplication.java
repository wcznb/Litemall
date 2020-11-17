package cn.edu.xmu.advertisement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.advertisement"})
@MapperScan("cn.edu.xmu.advertisement.mapper")
public class AdvertisementserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisementserviceApplication.class, args);
    }

}
