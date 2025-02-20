package com.cuit.common.config;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author xym
 * @since 2023-11-07 09:10
 * @description
 */

@Configuration
@EnableSwagger2  //开启swagger2
public class SwaggerConfig {

    //配置swagger 的Docket的Bean实例
    @Bean
    public Docket Shoezdocket(Environment environment){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("version1.0")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .securitySchemes(Arrays.asList(new ApiKey("token", "token", SecurityScheme.In.HEADER.name())))
                .securityContexts(Arrays.asList(securityContext()));
    }

    //配置swagger信息 apiInfo
    private ApiInfo apiInfo(){

        //作者信息
        Contact contact = new Contact("会展管理系统", "http://localhost:8000/exhibitionbk/swagger-ui/index.html", "3433539430@qq.com");

        return new ApiInfo("会展管理系统的SwaggerAPI文档",
                "小明想摆烂",
                "3.0",
                "http://localhost:8000/exhibitionbk/swagger-ui/index.html",
                contact, "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
    //下面三个为swagger的token支持
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                //.forPaths(PathSelectors.regex("/*.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference("token", authorizationScopes));
    }
}
