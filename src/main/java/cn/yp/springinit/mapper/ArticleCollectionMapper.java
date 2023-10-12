package cn.yp.springinit.mapper;

import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCollection;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


public interface ArticleCollectionMapper extends BaseMapper<ArticleCollection> {

    Page<Article> listCollectArticleByPage(Page<Object> objectPage, Wrapper<Article> queryWrapper, Long id);
}




