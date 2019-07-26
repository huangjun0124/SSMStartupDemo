package com.wishuok.config;

import com.google.common.base.Predicate;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@WebAppConfiguration
@EnableSwagger2
@ComponentScan(basePackages = "com.wishuok.controller")//配置Swagger要扫描的包
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //use controllers annotated with @Api
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class)) //Selection by RequestHandler
                .paths(PathSelectors.any()) //(paths()) // and by paths
                .build()
                .apiInfo(apiInfo());
    }

    //Here is an example where we select any api that matches one of these paths
    private Predicate<String> paths() {
        return or(
                regex("/business.*"),
                regex("/some.*"),
                regex("/contacts.*"),
                regex("/pet.*"),
                regex("/springsRestController.*"),
                regex("/test.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SSM Swagger接口信息")
                .description("Spring SpringMVC Mybatis 示例程序")
                .contact(new Contact("keith", "http://wishuok.com", "keithhuang0124@gmail.com"))
                .version("0.0.1")
                .build();
    }
}
