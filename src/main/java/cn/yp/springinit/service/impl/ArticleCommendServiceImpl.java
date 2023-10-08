package cn.yp.springinit.service.impl;

import cn.yp.springinit.service.ArticleCommendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.yp.springinit.model.domain.ArticleCommend;
import cn.yp.springinit.mapper.ArticleCommendMapper;
import org.springframework.stereotype.Service;

@Service
public class ArticleCommendServiceImpl extends ServiceImpl<ArticleCommendMapper, ArticleCommend>
    implements ArticleCommendService {

}




