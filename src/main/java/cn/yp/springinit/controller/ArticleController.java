package cn.yp.springinit.controller;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.model.context.ReqInfoContext;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.req.DeleteRequest;
import cn.yp.springinit.model.req.article.ArticleAddRequest;
import cn.yp.springinit.model.req.article.ArticleQueryRequest;
import cn.yp.springinit.model.req.article.ArticleUpdateRequest;
import cn.yp.springinit.model.vo.ArticleVO;
import cn.yp.springinit.service.ArticleService;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.JsonUtil;
import cn.yp.springinit.utils.ResUtil;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/article/api")
@Slf4j
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;


    // region 增删改查

    @PostMapping("/add")
    public BaseRes<Long> addArticle(@RequestBody ArticleAddRequest ArticleAddRequest) {
        if (ArticleAddRequest == null) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        Article article = new Article();
        BeanUtils.copyProperties(ArticleAddRequest, article);
        List<String> tags = ArticleAddRequest.getTags();
        if (tags != null) {
            article.setTags(JsonUtil.toStr(tags));
        }
        articleService.validArticle(article, true);
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        article.setUserId(userId);

        boolean result = articleService.save(article);
        ThrowUtil.throwIf(!result, ResCode.SYSTEM_ERROR);
        long newArticleId = article.getId();
        return ResUtil.buildSuccessRes(newArticleId);
    }

    @PostMapping("/delete")
    public BaseRes<Boolean> deleteArticle(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        // 判断是否存在
        Article oldArticle = articleService.getById(userId);
        ThrowUtil.throwIf(oldArticle == null, ResCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldArticle.getUserId().equals(userId) && !userService.isAdmin()) {
            throw new CustomException(ResCode.NO_AUTH_ERROR);
        }
        boolean b = articleService.removeById(userId);
        return ResUtil.buildSuccessRes(b);
    }

    @PostMapping("/update")
    public BaseRes<Boolean> editArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest) {
        if (articleUpdateRequest == null || articleUpdateRequest.getId() <= 0) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateRequest, article);
        List<String> tags = articleUpdateRequest.getTags();
        if (tags != null) {
            article.setTags(JsonUtil.toStr(tags));
        }
        // 参数校验
        articleService.validArticle(article, false);
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        // 判断是否存在
        Article oldArticle = articleService.getById(userId);
        ThrowUtil.throwIf(oldArticle == null, ResCode.NOT_FOUND_ERROR);
        // 仅本人可编辑
        if (!oldArticle.getUserId().equals(userId)) {
            throw new CustomException(ResCode.NO_AUTH_ERROR);
        }
        boolean result = articleService.updateById(article);
        return ResUtil.buildSuccessRes(result);
    }

    @GetMapping("/get/vo")
    public BaseRes<ArticleVO> getArticleVOById(long id) {
        if (id <= 0) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        Article article = articleService.getById(id);
        if (article == null) {
            throw new CustomException(ResCode.NOT_FOUND_ERROR);
        }
        return ResUtil.buildSuccessRes(articleService.getArticleVO(article));
    }

    @PostMapping("/list/page/vo")
    public BaseRes<Page<ArticleVO>> listArticleVOByPage(@RequestBody ArticleQueryRequest ArticleQueryRequest) {
        long current = ArticleQueryRequest.getCurrent();
        long size = ArticleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtil.throwIf(size > 20, ResCode.PARAM_ERROR);
        Page<Article> ArticlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(ArticleQueryRequest));
        return ResUtil.buildSuccessRes(articleService.getArticleVOPage(ArticlePage));
    }

    @PostMapping("/my/list/page/vo")
    public BaseRes<Page<ArticleVO>> listMyArticleVOByPage(@RequestBody ArticleQueryRequest ArticleQueryRequest) {
        if (ArticleQueryRequest == null) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        Long userId = ReqInfoContext.getReqInfo().getUserId();
        ArticleQueryRequest.setUserId(userId);
        long current = ArticleQueryRequest.getCurrent();
        long size = ArticleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtil.throwIf(size > 20, ResCode.PARAM_ERROR);
        Page<Article> ArticlePage = articleService.page(new Page<>(current, size),
                articleService.getQueryWrapper(ArticleQueryRequest));
        return ResUtil.buildSuccessRes(articleService.getArticleVOPage(ArticlePage));
    }

    // endregion

//    @PostMapping("/search/page/vo")
//    public BaseRes<Page<ArticleVO>> searchArticleVOByPage(@RequestBody ArticleQueryRequest ArticleQueryRequest) {
//        long size = ArticleQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtil.throwIf(size > 20, ResCode.PARAM_ERROR);
//        Page<Article> ArticlePage = articleService.searchFromEs(ArticleQueryRequest);
//        return ResUtil.buildSuccessRes(articleService.getArticleVOPage(ArticlePage));
//    }


}
