package cn.yp.springinit.service;

import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.model.dto.UserLoginDto;
import cn.yp.springinit.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;


public interface UserService extends IService<User> {

    void sendCheckCode(String phone);

    UserLoginDto userLogin(String phone, String checkCode);

    UserLoginDto userLoginWithPsw(String phone, String password);

    void setPassword(String password, String checkPassword);

    User getLoginUser();

    boolean isAdmin();

    UserVo getLoginUserVo();
}
