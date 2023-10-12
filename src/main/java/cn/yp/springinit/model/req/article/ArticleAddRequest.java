package cn.yp.springinit.model.req.article;

import lombok.Data;

import java.util.List;

/**
 * @author yp
 * @date: 2023/10/11
 */
@Data
public class ArticleAddRequest {
    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章分类
     */
    private Integer articleCategory;

    /**
     * 标签，可自定义
     */
    private List<String> tags;

}
