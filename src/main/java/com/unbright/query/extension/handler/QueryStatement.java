package com.unbright.query.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unbright.query.extension.QueryPage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created with IDEA
 * ProjectName: xl-farm
 * Date: 2020/4/15
 * Time: 8:42
 *
 * @author WZP
 * @version v1.0
 */
@Slf4j
@Deprecated
public class QueryStatement<T> {

    @Getter
    private QueryPage<T> page;

    private QueryStatement() {
    }

    public QueryStatement(QueryPage<T> page) {
        this.page = page;
    }

    /**
     * 组织查询语句.
     *
     * @param obj 对象
     * @return 查询对象
     */
    public QueryPage<T> createQuery(Object obj) {
        Class<?> clazz = obj.getClass();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        //先聚合and条件
        queryWrapper.nested(wrapper ->
                ObjectUtil.filterColumns(clazz, obj, false).forEach(field ->
                        createWrapper(field, obj, wrapper)));
        //聚合or的条件
        List<Field> fields = ObjectUtil.filterColumns(clazz, obj, true);
        if (fields.size() == 1) {
            queryWrapper.or().nested(wrapper -> createWrapper(fields.get(0), obj, wrapper));
        } else if (fields.size() > 1) {
            queryWrapper.nested(wrapper -> fields.forEach(field -> createWrapper(field, obj, wrapper.or())));
        }
        //排序字段
        ObjectUtil.filterSortColumns(clazz, obj)
                .forEach(field -> createWrapper(field, obj, queryWrapper));
        page.setWrapper(queryWrapper);
        return page;
    }

    /**
     * 根据字段注解创建条件构造器.
     *
     * @param field   字段
     * @param obj     对象
     * @param wrapper 条件构造器
     */
    public void createWrapper(Field field, Object obj, QueryWrapper<T> wrapper) {
        WrapperBuilder<T> builder = new DefaultWrapperBuilder<>(this.page);
        builder.build(field, obj, wrapper);
    }
}
