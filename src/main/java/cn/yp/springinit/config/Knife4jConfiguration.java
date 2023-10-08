package cn.yp.springinit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring 单体项目初始化模板")
                .description("一个基于 Spring Boot 集成 MyBatis-Plus、MySQL、Redis、ElasticSearch、MongoDB、Docker、RabbitMQ 等技术栈实现 SpringBoot 快速开发脚手架")
                .contact(new Contact("yp", "https://juejin.cn/user/167586915955549", "2429862664@qq.cn"))
                .version("1.0")
                .build();
    }

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                //分组名称
                .groupName("Spring-init 服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("cn.yp.springinit.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket testDocker() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .groupName("服务测试")
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.yp.springinit.test"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}