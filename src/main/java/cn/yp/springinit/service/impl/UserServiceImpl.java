package cn.yp.springinit.service.impl;

import cn.yp.springinit.model.Vo.UserVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public void sendCheckCode(String phone) {

    }

    @Override
    public Long userLogin(String userName, String checkCode) {
        return null;
    }

    @Override
    public Long userLoginWithPsw(String userName, String password) {
        return null;
    }

    @Override
    public void setPassword(String password, String checkPassword) {

    }

    @Override
    public UserVo getLoginUser(HttpServletRequest request) {
        return null;
    }
}




