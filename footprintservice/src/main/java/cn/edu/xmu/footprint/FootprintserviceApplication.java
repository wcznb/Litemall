package cn.edu.xmu.footprint;

import cn.edu.xmu.footprint.service.FootprintService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.footprint"})
@MapperScan("cn.edu.xmu.footprint.mapper")
@EnableDubbo //开启dubbo的注解支持
public class FootprintserviceApplication {

//    @Autowired
//    private FootprintService service;

    public static void main(String[] args) {
        SpringApplication.run(FootprintserviceApplication.class, args);
    }
}
