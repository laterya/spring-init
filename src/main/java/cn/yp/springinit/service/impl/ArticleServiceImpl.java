package cn.yp.springinit.service.impl;

import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.mapper.ArticleMapper;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.req.article.ArticleQueryRequest;
import cn.yp.springinit.model.vo.ArticleVO;
import cn.yp.springinit.service.ArticleService;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Override
    public void validArticle(Article article, boolean b) {
        if (article == null) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        String title = article.getTitle();
        String content = article.getContent();
        Integer articleCategory = article.getArticleCategory();
        String tags = article.getTags();

        if (b) {
            ThrowUtil.throwIf(StringUtils.isAnyBlank(title, content, tags), ResCode.PARAM_ERROR);
            ThrowUtil.throwIf(articleCategory == null || articleCategory <= 0, ResCode.PARAM_ERROR);
        }

        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new CustomException(ResCode.PARAM_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new CustomException(ResCode.PARAM_ERROR, "内容过长");
        }

    }

    @Override
    public ArticleVO getArticleVO(Article article) {

        return null;
    }

    @Override
    public Wrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        return null;
    }

    @Override
    public Page<ArticleVO> getArticleVOPage(Page<Article> articlePage) {
        return null;
    }
}




