package jp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//mapperScan用于mybatis
@MapperScan("jp.db.mybatis")
@SpringBootApplication
public class PmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmanagerApplication.class, args);
    }

}
