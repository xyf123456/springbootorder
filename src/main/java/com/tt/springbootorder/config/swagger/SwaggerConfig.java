package com.tt.springbootorder.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: com.tt.springbootorder.config.swagger.SwaggerConfig
 * @Description: swagger的核心配置类
 * @Author: Administrator
 * @CreateDate: 2019/7/28 0028 下午 9:03
 * @UpdateUser: Administrator
 * @Version: 1.0
 **/
//@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tt.springbootorder.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return  new ApiInfoBuilder()
                .title("超市订单系统接口")
                .termsOfServiceUrl("http://localhost:8080/")
                .version("0.0.1")
                .description("API 描述")
                .build();
    }
}
