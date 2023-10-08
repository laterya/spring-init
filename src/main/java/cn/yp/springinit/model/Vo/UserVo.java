package cn.yp.springinit.model.Vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yp
 * @date: 2023/10/9
 */
public class UserVo implements Serializable {

    private Long id;

    private String userName;


    private String phone;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}
