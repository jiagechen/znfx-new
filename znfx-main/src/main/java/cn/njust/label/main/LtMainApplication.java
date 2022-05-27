package cn.njust.label.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"cn.njust.label"})
@MapperScan(basePackages = {"com.njust.label"})
public class LtMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(LtMainApplication.class, args);
    }
}
