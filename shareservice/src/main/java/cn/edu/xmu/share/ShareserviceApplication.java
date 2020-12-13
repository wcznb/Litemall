package cn.edu.xmu.share;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.share"})
@MapperScan("cn.edu.xmu.share.mapper")
public class ShareserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShareserviceApplication.class, args);
    }

}
