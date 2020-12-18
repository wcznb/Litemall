package cn.edu.xmu.cart;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.cart"})
@MapperScan("cn.edu.xmu.cart.mapper")
@EnableDubbo //开启dubbo的注解支持
public class CartserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartserviceApplication.class, args);
    }

}
