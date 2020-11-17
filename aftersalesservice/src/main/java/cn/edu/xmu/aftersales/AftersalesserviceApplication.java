package cn.edu.xmu.aftersales;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.aftersales"})
@MapperScan("cn.edu.xmu.aftersales.mapper")
public class AftersalesserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AftersalesserviceApplication.class, args);
    }

}
