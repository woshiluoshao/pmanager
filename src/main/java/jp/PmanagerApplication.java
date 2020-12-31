package jp;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//mapperScan用于mybatis
//(exclude = SecurityAutoConfiguration.class)用于工作流
@MapperScan("jp.db.mybatis")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class PmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmanagerApplication.class, args);
    }

}
