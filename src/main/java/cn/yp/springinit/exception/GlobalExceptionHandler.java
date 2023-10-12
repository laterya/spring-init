package cn.yp.springinit.exception;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.utils.ResUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/10/8
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public BaseRes<?> customExceptionHandler(HttpServletRequest request, CustomException e) {
        log.error("CustomException: path: {}, msg: {}", request.getRequestURI(), e.getMessage());
        return ResUtil.buildFailRes(ResCode.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseRes<?> runtimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
        log.error("CustomException: path: {}, msg: {}", request.getRequestURI(), e.getMessage());
        return ResUtil.buildFailRes(ResCode.SYSTEM_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public BaseRes<?> ExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("CustomException: path: {}, msg: {}", request.getRequestURI(), e.getMessage());
        return ResUtil.buildFailRes(ResCode.SYSTEM_ERROR);
    }
}
