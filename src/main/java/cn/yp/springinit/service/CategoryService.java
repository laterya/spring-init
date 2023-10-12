package cn.yp.springinit.service;

import cn.yp.springinit.model.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface CategoryService extends IService<Category> {

    Integer addCategory(String categoryName);

    List<String> getAllCategory();

    Integer deleteCategory(Integer categoryId);

    Integer updateCategory(Integer categoryId, String categoryName);

    boolean isExist(Integer articleCategoryId);
}
