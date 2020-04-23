package com.unbright.pagination;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.unbright.pagination.dao")
@SpringBootApplication
public class PaginationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaginationApplication.class, args);
    }

}
