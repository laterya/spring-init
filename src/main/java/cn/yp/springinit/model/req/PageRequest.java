package cn.yp.springinit.model.req;

import cn.yp.springinit.constants.CommonConstant;
import lombok.Data;

/**
 * @author yp
 * @date: 2023/10/12
 */
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
