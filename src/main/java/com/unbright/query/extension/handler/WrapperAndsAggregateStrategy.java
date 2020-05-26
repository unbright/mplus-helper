package com.unbright.query.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:22
 * <p>
 * 聚合所有and条件.
 *
 * @author WZP
 * @version v1.0
 */
public class WrapperAndsAggregateStrategy<T> implements WrapperAggregateStrategy<T> {

    @Override
    public void aggregate(WrapperBuilder<T> builder, QueryWrapper<T> queryWrapper, Object obj) {
        Class<?> clazz = obj.getClass();
        queryWrapper.nested(wrapper ->
                ObjectUtil.filterColumns(clazz, obj, false)
                        .forEach(field -> builder.build(field, obj, wrapper)));
    }
}
