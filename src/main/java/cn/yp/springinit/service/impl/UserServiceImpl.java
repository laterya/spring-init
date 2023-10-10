package cn.yp.springinit.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.yp.springinit.cache.RedisClient;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.core.JwtHelper;
import cn.yp.springinit.core.PhoneHelper;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.mapper.UserMapper;
import cn.yp.springinit.model.context.ReqInfoContext;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.model.dto.UserLoginDto;
import cn.yp.springinit.model.vo.UserVo;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private JwtHelper jwtHelper;

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
    public UserLoginDto userLogin(String phone, String checkCode) {
        ThrowUtil.throwIf(checkCode.length() != 6, ResCode.PARAM_ERROR);
        String cacheCode = RedisClient.getStr(KEY_PREFIX + phone);
        if (!checkCode.equals(cacheCode)) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null) {
            user = register(phone);
        }
        String token = jwtHelper.genToken(user.getId());
        RedisClient.setStrWithExpire(token, String.valueOf(user.getId()), jwtHelper.getExpireTime() / 1000);
        return new UserLoginDto(user.getId(), token);
    }

    private User register(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setUserName("user" + RandomUtil.randomString(6));
        userMapper.insert(user);
        return user;
    }

    @Override
    public UserLoginDto userLoginWithPsw(String phone, String password) {
        if (!PhoneUtil.isPhone(phone)) {
            throw new CustomException(ResCode.PARAM_ERROR, "检查手机号");
        }
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        String encryptPassword = SecureUtil.md5("yp" + password);
        lqw.eq(User::getPhone, phone).eq(User::getPassword, encryptPassword);
        User user = userMapper.selectOne(lqw);
        String token = jwtHelper.genToken(user.getId());
        return new UserLoginDto(user.getId(), token);
    }

    @Override
    public void setPassword(String password, String checkPassword) {
        ThrowUtil.throwIf(!password.equals(checkPassword), ResCode.PARAM_ERROR, "两次密码不一致");
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        User user = new User();
        user.setId(userId);
        String encryptPassword = SecureUtil.md5("yp" + password);
        user.setPassword(encryptPassword);
        userMapper.updateById(user);
    }

    @Override
    public UserVo getLoginUser() {
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        User user = userMapper.selectById(userId);
        return UserVo.objToVo(user);
    }
}




