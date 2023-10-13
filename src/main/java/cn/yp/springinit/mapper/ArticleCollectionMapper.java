package cn.yp.springinit.mapper;

import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCollection;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


public interface ArticleCollectionMapper extends BaseMapper<ArticleCollection> {

    // todo 重写sql
    Page<Article> listCollectArticleByPage(Page<Object> objectPage, @Param(Constants.WRAPPER) Wrapper<Article> queryWrapper, Long id);
}




