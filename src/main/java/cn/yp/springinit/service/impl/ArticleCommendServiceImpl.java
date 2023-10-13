package cn.yp.springinit.service.impl;

import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.mapper.ArticleCommendMapper;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCommend;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.service.ArticleCommendService;
import cn.yp.springinit.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ArticleCommendServiceImpl extends ServiceImpl<ArticleCommendMapper, ArticleCommend>
        implements ArticleCommendService {

    @Resource
    private ArticleService articleService;

    @Override
    public int doArticleCommend(long articleId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Article article = articleService.getById(articleId);
        if (article == null) {
            throw new CustomException(ResCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        ArticleCommendService articleCommendService = (ArticleCommendService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return articleCommendService.doArticleThumbInner(userId, articleId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doArticleThumbInner(long userId, long articleId) {
        ArticleCommend articleCommend = new ArticleCommend();
        articleCommend.setUserId(userId);
        articleCommend.setArticleId(articleId);
        QueryWrapper<ArticleCommend> thumbQueryWrapper = new QueryWrapper<>(articleCommend);
        ArticleCommend oldArticleCommend = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldArticleCommend != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = articleService.update()
                        .eq("id", articleId)
                        .gt("commend_nums", 0)
                        .setSql("commend_nums = commend_nums - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new CustomException(ResCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(articleCommend);
            if (result) {
                // 点赞数 + 1
                result = articleService.update()
                        .eq("id", articleId)
                        .setSql("commend_nums = commend_nums + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new CustomException(ResCode.SYSTEM_ERROR);
            }
        }
    }
}




