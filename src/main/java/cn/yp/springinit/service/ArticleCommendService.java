package cn.yp.springinit.service;

import cn.yp.springinit.model.domain.ArticleCommend;
import cn.yp.springinit.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleCommendService extends IService<ArticleCommend> {

    int doArticleCommend(long articleId, User loginUser);

    int doArticleThumbInner(long userId, long articleId);
}
