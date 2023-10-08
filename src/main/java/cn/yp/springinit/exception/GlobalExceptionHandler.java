package cn.yp.springinit.exception;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.utils.ResUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yp
 * @date: 2023/10/8
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public BaseRes<?> customExceptionHandler(CustomException e) {
        log.error("CustomException: {}", e.getMessage());
        return ResUtil.buildFailRes(e.getResCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseRes<?> runtimeExceptionHandler(RuntimeException e) {
        log.info("RuntimeException: {}", e.getMessage());
        return ResUtil.buildFailRes(ResCode.SYSTEM_ERROR);
    }
}
