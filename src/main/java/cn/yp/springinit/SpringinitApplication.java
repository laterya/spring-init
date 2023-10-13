package cn.yp.springinit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ServletComponentScan
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan("cn.yp.springinit.mapper")
public class SpringinitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringinitApplication.class, args);
    }

}
