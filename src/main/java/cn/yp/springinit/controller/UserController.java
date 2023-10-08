package cn.yp.springinit.controller;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.model.Vo.UserVo;
import cn.yp.springinit.model.req.UserLoginPwdRequest;
import cn.yp.springinit.model.req.UserLoginRequest;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.PhoneUtil;
import cn.yp.springinit.utils.ResUtil;
import cn.yp.springinit.utils.ThrowUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/10/8
 */
@Api("用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("发送验证码")
    @GetMapping("/get/code")
    public BaseRes<Boolean> sendCode(@RequestParam("phone") String phone) {
        ThrowUtil.throwIf(StringUtils.isBlank(phone), ResCode.PARAM_ERROR);
        ThrowUtil.throwIf(!PhoneUtil.isValidPhoneNumber(phone), ResCode.PARAM_ERROR, "手机号格式错误");
        userService.sendCheckCode(phone);
        return ResUtil.buildSuccessRes(true);
    }

    @PostMapping("/login/code")
    public BaseRes<Long> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        String userName = userLoginRequest.getUserName();
        String checkCode = userLoginRequest.getCheckCode();

        ThrowUtil.throwIf(StringUtils.isAnyBlank(userName, checkCode), ResCode.PARAM_ERROR);
        Long userId = userService.userLogin(userName, checkCode);
        return ResUtil.buildSuccessRes(userId);
    }

    @PostMapping("/login/password")
    public BaseRes<Long> userLoginWithPassword(@RequestBody UserLoginPwdRequest userLoginPwdRequest) {
        String userName = userLoginPwdRequest.getUserName();
        String password = userLoginPwdRequest.getPassword();

        ThrowUtil.throwIf(StringUtils.isAnyBlank(userName, password), ResCode.PARAM_ERROR);
        Long userId = userService.userLoginWithPsw(userName, password);
        return ResUtil.buildSuccessRes(userId);
    }

    @PostMapping("/set/password")
    public BaseRes<Boolean> setPassword(@RequestParam("password") String password, @RequestParam("checkPassword") String checkPassword, HttpServletRequest request) {
        ThrowUtil.throwIf(StringUtils.isAnyBlank(password, checkPassword), ResCode.PARAM_ERROR);
        userService.setPassword(password, checkPassword);
        return ResUtil.buildSuccessRes(true);
    }

    @GetMapping("/get/loginMsg")
    public BaseRes<UserVo> getLoginUser(HttpServletRequest request) {
        return ResUtil.buildSuccessRes(userService.getLoginUser(request));
    }
}
