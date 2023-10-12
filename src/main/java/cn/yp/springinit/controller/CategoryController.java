package cn.yp.springinit.controller;

import cn.yp.springinit.common.BaseRes;
import cn.yp.springinit.common.ResCode;
import cn.yp.springinit.model.req.category.CategoryAddRequest;
import cn.yp.springinit.service.CategoryService;
import cn.yp.springinit.utils.ResUtil;
import cn.yp.springinit.utils.ThrowUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yp
 * @date: 2023/10/11
 */
@RestController
@RequestMapping("/category/api")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    // region 仅限管理员可操作，操作分类栏目
    @PostMapping("/add")
    public BaseRes<Integer> addCategory(@RequestBody CategoryAddRequest categoryAddRequest) {
        String categoryName = categoryAddRequest.getCategoryName();
        ThrowUtil.throwIf(StringUtils.isBlank(categoryName), ResCode.PARAM_ERROR);

        Integer categoryId = categoryService.addCategory(categoryName);

        return ResUtil.buildSuccessRes(categoryId);
    }

    @DeleteMapping
    public BaseRes<Integer> deleteCategory(@RequestParam("categoryId") Integer categoryId) {
        ThrowUtil.throwIf(categoryId == null || categoryId <= 0, ResCode.PARAM_ERROR);
        Integer res = categoryService.deleteCategory(categoryId);
        return ResUtil.buildSuccessRes(res);
    }

    @PutMapping
    public BaseRes<Integer> updateCategory(@RequestParam("categoryId") Integer categoryId,
                                           @RequestParam("categoryName") String categoryName) {
        ThrowUtil.throwIf(categoryId == null || categoryId <= 0, ResCode.PARAM_ERROR);
        ThrowUtil.throwIf(StringUtils.isBlank(categoryName), ResCode.PARAM_ERROR);
        Integer res = categoryService.updateCategory(categoryId, categoryName);
        return ResUtil.buildSuccessRes(res);
    }

    // endregion
    @GetMapping
    public BaseRes<List<String>> getCategory() {
        List<String> res = categoryService.getAllCategory();
        return ResUtil.buildSuccessRes(res);
    }
}
