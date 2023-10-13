package cn.yp.springinit.service;

import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCollection;
import cn.yp.springinit.model.domain.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleCollectionService extends IService<ArticleCollection> {

    int doArticleCollect(long articleId, User loginUser);

    Page<Article> listCollectArticleByPage(Page<Object> objectPage, Wrapper<Article> queryWrapper, Long id);

    int doArticleCollectInner(long userId, long articleId);
}
