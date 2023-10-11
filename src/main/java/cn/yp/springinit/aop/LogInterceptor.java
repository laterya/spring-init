package cn.yp.springinit.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author yp
 * @date: 2023/10/11
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {



    @Around("execution(* cn.yp.springinit.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch("日志计时器");
        stopWatch.start();
        // 请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String requestURI = request.getRequestURI();
        // 唯一id
        String requestId = UUID.randomUUID().toString();
        // 请求参数
        Object[] args = proceedingJoinPoint.getArgs();
        String reqParam = "[" + StringUtils.join(args, ",") + "]";
        // 输出请求日志
        log.info("request start: id: {}, path: {}, ip: {}, params: {}", requestId, requestURI, request.getRemoteHost(), reqParam);
        Object res = proceedingJoinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);
        return res;
    }
}
