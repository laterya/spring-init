package cn.yp.springinit.common;

import lombok.Getter;

/**
 * @author yp
 * @date: 2023/10/8
 */
@Getter
public enum ResCode {
    SUCCESS(0, "success"),
    NOT_LOGIN(10001, "not login"),
    NO_AUTH(10002, "no auth"),
    SYSTEM_ERROR(10003, "system error"),
    PARAM_ERROR(10004, "param error"),
    JWT_ERROR(10005, "jwt error"),
    ;

    private int code;

    private String msg;

    ResCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
