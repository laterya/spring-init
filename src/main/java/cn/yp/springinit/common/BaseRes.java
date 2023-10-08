package cn.yp.springinit.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yp
 * @date: 2023/10/8
 */
@Data
@Builder
public class BaseRes<T> implements Serializable {

    private static final long serialVersionUID = 7350619227496221091L;

    private int code;

    private String msg;

    private T data;
}
