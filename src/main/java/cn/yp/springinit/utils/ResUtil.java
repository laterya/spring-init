package cn.yp.springinit.utils;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;

/**
 * @author yp
 * @date: 2023/10/8
 */
public class ResUtil {

    public static <T> BaseRes<T> buildSuccessRes(T data) {
        return BaseRes.<T>builder()
                .code(ResCode.SUCCESS.getCode())
                .msg(ResCode.SUCCESS.getMsg())
                .data(data)
                .build();
    }

    public static <T> BaseRes<T> buildFailRes(ResCode ResCode) {
        return BaseRes.<T>builder()
                .code(ResCode.getCode())
                .msg(ResCode.getMsg())
                .build();
    }

    public static <T> BaseRes<T> buildFailRes(ResCode ResCode, String msg) {
        return BaseRes.<T>builder()
                .code(ResCode.getCode())
                .msg(msg)
                .build();
    }
}
