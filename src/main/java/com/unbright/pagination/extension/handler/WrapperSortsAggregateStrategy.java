package com.unbright.pagination.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:38
 * <p>
 * 聚合排序字段.
 *
 * @author WZP
 * @version v1.0
 */
public class WrapperSortsAggregateStrategy<T> implements WrapperAggregateStrategy<T> {

    @Override
    public void aggregate(WrapperBuilder<T> builder, QueryWrapper<T> queryWrapper, Object obj) {
        ObjectUtil.filterSortColumns(obj.getClass(), obj)
                .forEach(field -> builder.build(field, obj, queryWrapper));
    }
}
