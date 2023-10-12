package cn.yp.springinit.model.req.user;

import lombok.Data;

/**
 * @author yp
 * @date: 2023/10/9
 */
@Data
public class UserLoginRequest {

    private String phone;

    private String checkCode;
}
