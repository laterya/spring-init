package cn.yp.springinit.service.impl;


import cn.yp.springinit.mapper.ArticleCollectionMapper;
import cn.yp.springinit.model.domain.ArticleCollection;
import cn.yp.springinit.service.ArticleCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ArticleCollectionServiceImpl extends ServiceImpl<ArticleCollectionMapper, ArticleCollection>
    implements ArticleCollectionService {

}




