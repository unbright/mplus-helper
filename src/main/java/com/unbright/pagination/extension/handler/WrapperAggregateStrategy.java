package com.unbright.pagination.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:16
 * <p>
 * 条件聚合器.
 *
 * @author WZP
 * @version v1.0
 */
public interface WrapperAggregateStrategy<T> {

    /**
     * 聚合对象的字段条件.
     *
     * @param builder      条件构造器.
     * @param queryWrapper 查询条件
     * @param obj          对象
     */
    void aggregate(WrapperBuilder<T> builder, QueryWrapper<T> queryWrapper, Object obj);

}
