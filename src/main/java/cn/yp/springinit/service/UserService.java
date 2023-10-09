package cn.yp.springinit.service;

import cn.yp.springinit.model.Vo.UserVo;
import cn.yp.springinit.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    void sendCheckCode(String phone);

    Long userLogin(String phone, String checkCode);

    Long userLoginWithPsw(String userName, String password);

    void setPassword(String password, String checkPassword);

    UserVo getLoginUser(HttpServletRequest request);
}
