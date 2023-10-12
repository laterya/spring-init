package cn.yp.springinit.filter;

import cn.yp.springinit.cache.RedisClient;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.core.JwtHelper;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.model.context.ReqInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author yp
 * @date: 2023/10/10
 */

/**
 * 校验是否带有token并保存
 */
@WebFilter(urlPatterns = "/*", filterName = "reqRecordFilter", asyncSupported = true)
@Slf4j
public class ReqCheckFilter implements Filter {

    @Resource
    private JwtHelper jwtHelper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
        if (isAccess(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 校验token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            log.info(request.getRequestURI());
            throw new CustomException(ResCode.NOT_LOGIN);
        }
        String userIdByToken = String.valueOf(jwtHelper.getUserIdByToken(token));
        // 查询 Redis
        String userId = RedisClient.getStr(token);
        if (userId == null) {
            throw new CustomException(ResCode.LOGIN_EXPIRE, "登陆已过期，请重新登陆");
        }
        if (!userIdByToken.equals(userId)) {
            throw new CustomException(ResCode.SYSTEM_ERROR);
        }
        // 初始化登陆信息
        ReqInfoContext.ReqInfo reqInfo = new ReqInfoContext.ReqInfo();
        reqInfo.setPath(path);
        reqInfo.setToken(token);
        reqInfo.setUserId(Long.valueOf(userId));
        ReqInfoContext.addReqInfo(reqInfo);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // todo 优化路径过滤规则
    private boolean isAccess(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/doc.html")
                || request.getRequestURI().contains("webjars")
                || request.getRequestURI().endsWith("swagger-resources")
                || request.getRequestURI().endsWith("favicon.ico")
                || request.getRequestURI().endsWith("/v2/api-docs")

                || request.getRequestURI().equals("/user/api/getCode")
                || request.getRequestURI().equals("/category/api/get")
                || request.getRequestURI().contains("login");

    }
}
