package cn.yp.springinit.service.impl;

import cn.yp.springinit.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.yp.springinit.model.domain.Article;
import cn.yp.springinit.mapper.ArticleMapper;
import org.springframework.stereotype.Service;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService {

}




