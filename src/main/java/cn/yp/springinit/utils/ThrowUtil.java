package cn.yp.springinit.utils;

import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;

/**
 * @author yp
 * @date: 2023/10/8
 */
public class ThrowUtil {
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    public static void throwIf(boolean condition, ResCode resCode) {
        if (condition) {
            throw new CustomException(resCode);
        }
    }

    public static void throwIf(boolean condition, ResCode resCode, String message) {
        if (condition) {
            throw new CustomException(resCode, message);
        }
    }
}
