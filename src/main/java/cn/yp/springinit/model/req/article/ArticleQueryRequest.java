package cn.yp.springinit.model.req.article;

import cn.yp.springinit.model.req.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author yp
 * @date: 2023/10/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest {

    /**
     * 作者id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String searchText;

    /**
     * 文章分类
     */
    private Integer articleCategory;

    /**
     * 标签，可自定义
     */
    private List<String> tags;

}
