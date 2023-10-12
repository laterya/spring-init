package cn.yp.springinit.model.req.collect;

import cn.yp.springinit.model.req.PageRequest;
import cn.yp.springinit.model.req.article.ArticleQueryRequest;
import lombok.Data;

/**
 * @author yp
 * @date: 2023/10/12
 */
@Data
public class ArticleCollectQueryRequest extends PageRequest {
    /**
     * 用户id
     */
    private Long userId;


    public ArticleQueryRequest getArticleQueryRequest() {
        return null;
    }
}
