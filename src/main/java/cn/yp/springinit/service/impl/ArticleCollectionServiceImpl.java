package cn.yp.springinit.service.impl;


import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.mapper.ArticleCollectionMapper;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCollection;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.service.ArticleCollectionService;
import cn.yp.springinit.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ArticleCollectionServiceImpl extends ServiceImpl<ArticleCollectionMapper, ArticleCollection>
        implements ArticleCollectionService {

    @Resource
    private ArticleService articleService;

    @Override
    public int doArticleCollect(long articleId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Article article = articleService.getById(articleId);
        if (article == null) {
            throw new CustomException(ResCode.NOT_FOUND_ERROR);
        }
        // 是否已收藏
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        ArticleCollectionService articleCollectionService = (ArticleCollectionService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return articleCollectionService.doArticleCollectInner(userId, articleId);
        }
    }

    @Override
    public Page<Article> listCollectArticleByPage(Page<Object> objectPage, Wrapper<Article> queryWrapper, Long id) {
        if (id <= 0) {
            return new Page<>();
        }
        return baseMapper.listCollectArticleByPage(objectPage, queryWrapper, id);
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
    public int doArticleCollectInner(long userId, long articleId) {
        ArticleCollection articleCollection = new ArticleCollection();
        articleCollection.setUserId(userId);
        articleCollection.setArticleId(articleId);
        QueryWrapper<ArticleCollection> articleCollectionQueryWrapper = new QueryWrapper<>(articleCollection);
        ArticleCollection oldArticleCollection = this.getOne(articleCollectionQueryWrapper);
        boolean result;
        // 已收藏
        if (oldArticleCollection != null) {
            result = this.remove(articleCollectionQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = articleService.update()
                        .eq("id", articleId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new CustomException(ResCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(articleCollection);
            if (result) {
                // 帖子收藏数 + 1
                result = articleService.update()
                        .eq("id", articleId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new CustomException(ResCode.SYSTEM_ERROR);
            }
        }
    }
}




