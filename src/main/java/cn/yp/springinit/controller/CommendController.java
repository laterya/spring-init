package cn.yp.springinit.controller;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.model.req.commend.CommendAddRequest;
import cn.yp.springinit.service.ArticleCommendService;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.ResUtil;
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
@RequestMapping("/commend/api")
public class CommendController {

    @Resource
    private ArticleCommendService articleCommendService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param commendAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseRes<Integer> doThumb(@RequestBody CommendAddRequest commendAddRequest) {
        if (commendAddRequest == null || commendAddRequest.getArticleId() <= 0) {
            throw new CustomException(ResCode.PARAM_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser();
        long articleId = commendAddRequest.getArticleId();
        int result = articleCommendService.doArticleCommend(articleId, loginUser);
        return ResUtil.buildSuccessRes(result);
    }

}
