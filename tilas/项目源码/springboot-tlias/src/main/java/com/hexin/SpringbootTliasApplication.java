package com.hexin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan//开启servlet组件支持（ Filter过滤器）
@SpringBootApplication
public class SpringbootTliasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTliasApplication.class, args);
    }

}
