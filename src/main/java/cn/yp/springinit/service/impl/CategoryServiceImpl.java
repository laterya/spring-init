package cn.yp.springinit.service.impl;

import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.mapper.CategoryMapper;
import cn.yp.springinit.model.domain.Category;
import cn.yp.springinit.service.CategoryService;
import cn.yp.springinit.utils.ThrowUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Integer addCategory(String categoryName) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public List<String> getAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(wrapper);
        ThrowUtil.throwIf(categories == null || categories.size() == 0, ResCode.SYSTEM_ERROR, "分类表为空");
        List<String> res = new ArrayList<>();
        for (Category category : categories) {
            res.add(category.getCategoryName());
        }
        return res;
    }

    @Override
    public Integer deleteCategory(Integer categoryId) {
        return categoryMapper.deleteById(categoryId);
    }

    @Override
    public Integer updateCategory(Integer categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setCategoryName(categoryName);
        return categoryMapper.updateById(category);
    }

    @Override
    public boolean isExist(Integer articleCategoryId) {
        ThrowUtil.throwIf(articleCategoryId <= 0, ResCode.PARAM_ERROR, "分类id错误");
        Category category = categoryMapper.selectById(articleCategoryId);
        return category != null;
    }
}




