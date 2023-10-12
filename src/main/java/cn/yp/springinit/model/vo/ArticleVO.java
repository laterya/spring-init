package cn.yp.springinit.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author yp
 * @date: 2023/10/12
 */
@Data
public class ArticleVO {
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
    private String content;

    /**
     * 文章分类
     */
    private Integer articleCategory;

    /**
     * 标签，可自定义
     */
    private String tags;

    /**
     * 点赞数
     */
    private Integer commendNums;

    /**
     * 收藏数
     */
    private Integer collectionNums;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人信息
     */
    private UserVo userVo;

    /**
     * 是否点赞
     */
    private Boolean hadCommend;

    /**
     * 是否收藏
     */
    private Boolean hadCollection;
}
