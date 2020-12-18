package cn.edu.xmu.favorite;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.favorite"})
@MapperScan("cn.edu.xmu.favorite.mapper")
@EnableDubbo //开启dubbo的注解支持
public class FavoriteserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavoriteserviceApplication.class, args);
    }

}
