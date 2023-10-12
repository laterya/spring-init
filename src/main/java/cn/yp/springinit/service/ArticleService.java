package cn.yp.springinit.service;

import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.req.article.ArticleQueryRequest;
import cn.yp.springinit.model.vo.ArticleVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleService extends IService<Article> {

    /**
     * 参数校验
     * @param article
     * @param b true 新增 false 更新
     */
    void validArticle(Article article, boolean b);

    ArticleVO getArticleVO(Article article);

    Wrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest);

    Page<ArticleVO> getArticleVOPage(Page<Article> articlePage);
}
