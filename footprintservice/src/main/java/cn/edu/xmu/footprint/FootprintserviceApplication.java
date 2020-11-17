package cn.edu.xmu.footprint;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.footprint"})
@MapperScan("cn.edu.xmu.footprint.mapper")
public class FootprintserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootprintserviceApplication.class, args);
    }

}
