package cn.yp.springinit.service.impl;


import cn.yp.springinit.mapper.ArticleCollectionMapper;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCollection;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.service.ArticleCollectionService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleCollectionServiceImpl extends ServiceImpl<ArticleCollectionMapper, ArticleCollection>
        implements ArticleCollectionService {




    @Override
    public int doArticleCollect(long articleId, User loginUser) {
        return 0;
    }

    @Override
    public Page<Article> listCollectArticleByPage(Page<Object> objectPage, Wrapper<Article> queryWrapper, Long id) {
        return null;
    }
}




