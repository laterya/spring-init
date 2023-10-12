package cn.yp.springinit.exception;

import cn.yp.springinit.common.ResCode;

/**
 * @author yp
 * @date: 2023/10/8
 */
public class CustomException extends RuntimeException {
    private final int resCode;

    public CustomException(int code, String msg) {
        super(msg);
        this.resCode = code;
    }

    public CustomException(ResCode resCode) {
        super(resCode.getMsg());
        this.resCode = resCode.getCode();
    }

    public CustomException(ResCode resCode, String msg) {
        super(msg);
        this.resCode = resCode.getCode();
    }

    public int getResCode() {
        return resCode;
    }
}
