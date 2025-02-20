package com.cuit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.cuit.**.mapper")
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling    //开启调度任务
@SpringBootApplication
public class ExhibitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExhibitionApplication.class, args);
        System.out.println("\n" +
                "|￣￣￣￣￣￣￣￣￣￣￣|  \n" +
                "    会展管理系统启动成功！！      \n" +
                "|＿＿＿＿＿＿＿＿＿＿＿|  \n" +
                "     \\ (•◡•) //         \n" +
                "      \\     //           \n" +
                "         ---              \n" +
                "        |   |  ");
    }
}


