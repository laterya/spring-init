package cn.yp.springinit.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.yp.springinit.cache.RedisClient;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.core.PhoneHelper;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.mapper.UserMapper;
import cn.yp.springinit.model.Vo.UserVo;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    private final String KEY_PREFIX = "springinit";

    @Override
    public void sendCheckCode(String phone) {
        String code = RandomUtil.randomNumbers(6);
        RedisClient.setStrWithExpire(KEY_PREFIX + phone, code, 180L);
        try {
            Boolean bool = PhoneHelper.sendCode(phone, code);
            ThrowUtil.throwIf(!bool, ResCode.SEND_CODE_ERROR);
        } catch (Exception exception) {
            throw new CustomException(ResCode.SYSTEM_ERROR, "发送验证码失败");
        }
    }

    @Override
    public Long userLogin(String phone, String checkCode) {
        ThrowUtil.throwIf(checkCode.length() != 6, ResCode.PARAM_ERROR);
        String cacheCode = RedisClient.getStr(KEY_PREFIX + phone);
        if (!checkCode.equals(cacheCode)) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null) {
            user = register(phone);
        }
        // todo 将token值进行返回
        return user.getId();
    }

    private User register(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setUserName("user" + RandomUtil.randomString(6));
        userMapper.insert(user);
        return user;
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




