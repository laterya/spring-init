package cn.yp.springinit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yp
 * @date: 2023/10/10
 */
@Data
@AllArgsConstructor
public class UserLoginDto {
    private Long userId;

    private String token;
}
