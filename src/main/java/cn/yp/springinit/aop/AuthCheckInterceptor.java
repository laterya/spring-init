package cn.yp.springinit.aop;

import cn.yp.springinit.annotation.AuthCheck;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.model.context.ReqInfoContext;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.model.enums.UserRoleEnum;
import cn.yp.springinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/10/12
 */
@Aspect
@Component
@Slf4j
public class AuthCheckInterceptor {
    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 当前登录用户
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        User loginUser = userService.getById(userId);
        // 必须有该权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (mustUserRoleEnum == null) {
                throw new CustomException(ResCode.NO_AUTH_ERROR);
            }
            String userRole = loginUser.getUserRole();
            // 如果被封号，直接拒绝
            if (UserRoleEnum.BAN.equals(mustUserRoleEnum)) {
                throw new CustomException(ResCode.NO_AUTH_ERROR);
            }
            // 必须有管理员权限
            if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!mustRole.equals(userRole)) {
                    throw new CustomException(ResCode.NO_AUTH_ERROR);
                }
            }
        }

        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}
