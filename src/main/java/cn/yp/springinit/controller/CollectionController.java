package cn.yp.springinit.controller;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.model.req.article.ArticleQueryRequest;
import cn.yp.springinit.model.req.collect.ArticleCollectAddRequest;
import cn.yp.springinit.model.req.collect.ArticleCollectQueryRequest;
import cn.yp.springinit.model.vo.ArticleVO;
import cn.yp.springinit.service.ArticleCollectionService;
import cn.yp.springinit.service.ArticleService;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.ResUtil;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yp
 * @date: 2023/10/12
 */
@RestController
@RequestMapping("/collection/api")
public class CollectionController {

    @Resource
    private ArticleCollectionService articleCollectionService;

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    @PostMapping("/")
    public BaseRes<Integer> doArticleCollect(@RequestBody ArticleCollectAddRequest articleCollectAddRequest) {
        if (articleCollectAddRequest == null || articleCollectAddRequest.getArticleId() <= 0) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser();
        long articleId = articleCollectAddRequest.getArticleId();
        int result = articleCollectionService.doArticleCollect(articleId, loginUser);
        return ResUtil.buildSuccessRes(result);
    }

    @PostMapping("/my/list/page")
    public BaseRes<Page<ArticleVO>> listMyCollectArticleByPage(@RequestBody ArticleQueryRequest postQueryRequest) {
        if (postQueryRequest == null) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser();
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtil.throwIf(size > 20, ResCode.PARAM_ERROR);
        Page<Article> postPage = articleCollectionService.listCollectArticleByPage(new Page<>(current, size),
                articleService.getQueryWrapper(postQueryRequest), loginUser.getId());
        return ResUtil.buildSuccessRes(articleService.getArticleVOPage(postPage));
    }

    @PostMapping("/list/page")
    public BaseRes<Page<ArticleVO>> listCollectArticleByPage(@RequestBody ArticleCollectQueryRequest postCollectQueryRequest) {
        if (postCollectQueryRequest == null) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        long current = postCollectQueryRequest.getCurrent();
        long size = postCollectQueryRequest.getPageSize();
        Long userId = postCollectQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtil.throwIf(size > 20 || userId == null, ResCode.PARAM_ERROR);
        Page<Article> postPage = articleCollectionService.listCollectArticleByPage(new Page<>(current, size),
                articleService.getQueryWrapper(postCollectQueryRequest.getArticleQueryRequest()), userId);
        return ResUtil.buildSuccessRes(articleService.getArticleVOPage(postPage));
    }
}
