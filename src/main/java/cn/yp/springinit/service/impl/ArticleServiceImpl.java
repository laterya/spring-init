package cn.yp.springinit.service.impl;

import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.exception.CustomException;
import cn.yp.springinit.mapper.ArticleCollectionMapper;
import cn.yp.springinit.mapper.ArticleCommendMapper;
import cn.yp.springinit.mapper.ArticleMapper;
import cn.yp.springinit.model.context.ReqInfoContext;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.model.domain.ArticleCollection;
import cn.yp.springinit.model.domain.ArticleCommend;
import cn.yp.springinit.model.domain.User;
import cn.yp.springinit.model.req.article.ArticleQueryRequest;
import cn.yp.springinit.model.vo.ArticleVO;
import cn.yp.springinit.model.vo.UserVo;
import cn.yp.springinit.service.ArticleService;
import cn.yp.springinit.service.UserService;
import cn.yp.springinit.utils.JsonUtil;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserService userService;

    @Resource
    private ArticleCollectionMapper articleCollectionMapper;

    @Resource
    private ArticleCommendMapper articleCommendMapper;

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
        ArticleVO articleVO = ArticleVO.objToVo(article);
        User user = userService.getById(articleVO.getUserId());
        UserVo userVo = UserVo.objToVo(user);
        articleVO.setUserVo(userVo);

        Long userId = ReqInfoContext.getReqInfo().getUserId();
        LambdaQueryWrapper<ArticleCollection> articleCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getArticleId, article.getId());
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getUserId, userId);
        ArticleCollection articleCollection = articleCollectionMapper.selectOne(articleCollectionLambdaQueryWrapper);
        if (articleCollection != null) {
            articleVO.setHadCollection(true);
        } else {
            articleVO.setHadCollection(false);
        }
        LambdaQueryWrapper<ArticleCommend> articleCommendLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCommendLambdaQueryWrapper.eq(ArticleCommend::getArticleId, article.getId());
        articleCommendLambdaQueryWrapper.eq(ArticleCommend::getUserId, userId);
        ArticleCommend articleCommend = articleCommendMapper.selectOne(articleCommendLambdaQueryWrapper);
        if (articleCommend != null) {
            articleVO.setHadCommend(true);
        } else {
            articleVO.setHadCommend(false);
        }
        return articleVO;
    }

    /**
     * 获取查询包装类
     *
     * @param articleQueryRequest
     * @return
     */
    @Override
    public Wrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (articleQueryRequest != null) {

            Long userId = articleQueryRequest.getUserId();
            String title = articleQueryRequest.getTitle();
            String searchText = articleQueryRequest.getSearchText();
            Integer articleCategory = articleQueryRequest.getArticleCategory();
            List<String> tags = articleQueryRequest.getTags();
            if (StringUtils.isNotBlank(searchText)) {
                articleLambdaQueryWrapper.like(Article::getTitle, searchText).or().like(Article::getContent, searchText);
            }
            articleLambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(userId) && userId > 0, Article::getUserId, userId);
            articleLambdaQueryWrapper.like(StringUtils.isNotBlank(title), Article::getTitle, title);
            articleLambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(articleCategory) && articleCategory > 0, Article::getArticleCategory, articleCategory);
            if (CollectionUtils.isNotEmpty(tags)) {
                for (String tag : tags) {
                    articleLambdaQueryWrapper.like(Article::getTags, tag);
                }
            }
            articleLambdaQueryWrapper.eq(Article::getIsDeleted, false);
        }
        return articleLambdaQueryWrapper;
    }

    @Override
    public Page<ArticleVO> getArticleVoPage(Page<Article> articlePage) {
        List<Article> records = articlePage.getRecords();
        log.info("records:{}", records);
        Page<ArticleVO> articleVOPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        if (CollectionUtils.isEmpty(records)) {
            return articleVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = records.stream().map(Article::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));
        // 2. 获取用户点赞，收藏情况
        Map<Long, Boolean> articleIdHadCollectionMap = new HashMap<>();
        Map<Long, Boolean> articleIdHadCommendMap = new HashMap<>();

        Long userId = ReqInfoContext.getReqInfo().getUserId();
        Set<Long> articleIds = records.stream().map(Article::getId).collect(Collectors.toSet());

        LambdaQueryWrapper<ArticleCommend> articleCommendLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCommendLambdaQueryWrapper.in(ArticleCommend::getArticleId, articleIds);
        articleCommendLambdaQueryWrapper.eq(ArticleCommend::getUserId, userId);
        List<ArticleCommend> articleCommendList = articleCommendMapper.selectList(articleCommendLambdaQueryWrapper);
        articleCommendList.forEach(articleCommend -> articleIdHadCommendMap.put(articleCommend.getArticleId(), true));
        // 获取收藏
        LambdaQueryWrapper<ArticleCollection> articleCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCollectionLambdaQueryWrapper.in(ArticleCollection::getArticleId, articleIds);
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getUserId, userId);
        List<ArticleCollection> articleCollectionsList = articleCollectionMapper.selectList(articleCollectionLambdaQueryWrapper);
        articleCollectionsList.forEach(articleCollection -> articleIdHadCollectionMap.put(articleCollection.getArticleId(), true));
        // 3. 填充信息
        List<ArticleVO> articleVOS = records.stream().map(article -> {
            ArticleVO articleVO = new ArticleVO();
            User user = null;
            Long id = article.getUserId();
            if (userIdUserListMap.containsKey(id)) {
                user = userIdUserListMap.get(id).get(0);
            }
            articleVO.setUserVo(UserVo.objToVo(user));
            articleVO.setHadCollection(articleIdHadCollectionMap.getOrDefault(article.getId(), false));
            articleVO.setHadCommend(articleIdHadCommendMap.getOrDefault(article.getId(), false));
            articleVO.setTags(JsonUtil.toObj(article.getTags(), List.class));
            BeanUtils.copyProperties(article, articleVO);
            return articleVO;
        }).collect(Collectors.toList());
        articleVOPage.setRecords(articleVOS);
        log.info("articleVOPage:{}", articleVOPage.getRecords());
        return articleVOPage;
    }
}




