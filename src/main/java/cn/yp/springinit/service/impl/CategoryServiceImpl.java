package cn.yp.springinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.yp.springinit.model.domain.Category;
import cn.yp.springinit.service.CategoryService;
import cn.yp.springinit.mapper.CategoryMapper;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




