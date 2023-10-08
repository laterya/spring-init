package cn.yp.springinit.exception;

import cn.yp.springinit.common.ResCode;

/**
 * @author yp
 * @date: 2023/10/8
 */
public class CustomException extends RuntimeException {
    private final ResCode resCode;

    public CustomException(ResCode resCode) {
        super(resCode.getMsg());
        this.resCode = resCode;
    }

    public CustomException(ResCode resCode, String msg) {
        super(msg);
        this.resCode = resCode;
    }

    public ResCode getResCode() {
        return resCode;
    }
}
