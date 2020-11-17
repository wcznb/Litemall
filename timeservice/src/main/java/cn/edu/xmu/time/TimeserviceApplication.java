package cn.edu.xmu.time;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.time"})
@MapperScan("cn.edu.xmu.time.mapper")
public class TimeserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeserviceApplication.class, args);
    }

}
