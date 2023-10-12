package cn.yp.springinit.model.req.commend;

import lombok.Data;

/**
 * @author yp
 * @date: 2023/10/12
 */
@Data
public class CommendAddRequest {
    /**
     * 点赞文章id
     */
    private Long articleId;

}
