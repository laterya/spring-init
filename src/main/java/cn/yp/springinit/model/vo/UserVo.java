package cn.yp.springinit.model.vo;

import cn.yp.springinit.model.domain.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yp
 * @date: 2023/10/9
 */
@Data
public class UserVo implements Serializable {

    private Long id;

    private String userName;


    private String phone;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public static UserVo objToVo(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
