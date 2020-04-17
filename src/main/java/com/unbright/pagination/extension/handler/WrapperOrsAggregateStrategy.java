package com.unbright.pagination.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:35
 * <p>
 * 聚合所有or条件.
 *
 * @author WZP
 * @version v1.0
 */
public class WrapperOrsAggregateStrategy<T> implements WrapperAggregateStrategy<T> {

    @Override
    public void aggregate(WrapperBuilder<T> builder, QueryWrapper<T> queryWrapper, Object obj) {
        List<Field> fields = ObjectUtil.filterColumns(obj.getClass(), obj, true);
        if (fields.size() == 1) {
            queryWrapper.or().nested(wrapper -> builder.build(fields.get(0), obj, wrapper));
        } else if (fields.size() > 1) {
            queryWrapper.nested(wrapper -> fields.forEach(field -> builder.build(field, obj, wrapper.or())));
        }
    }
}
