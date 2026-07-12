package com.itbin;

//import com.example.EnableHeaderConfig;
//import com.example.TokenParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@ServletComponentScan//开启了SpringBoot对Servlet组件的支持
//@ComponentScan(basePackages = {"com.example", "com.itbin"})//扫描第三方工具包的组件，默认扫描会失效
//@Import(TokenParser.class)
//@EnableHeaderConfig
@SpringBootApplication
public class TliasWebManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TliasWebManagementApplication.class, args);
    }

}
