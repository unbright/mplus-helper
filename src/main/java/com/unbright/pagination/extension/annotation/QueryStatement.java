package com.unbright.pagination.extension.annotation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.unbright.pagination.extension.QueryPage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
                filterColumns(clazz, obj, false).forEach(field ->
                        createWrapper(field, obj, wrapper)));
        //聚合or的条件
        List<Field> fields = filterColumns(clazz, obj, true);
        if (fields.size() == 1) {
            queryWrapper.or().nested(wrapper -> createWrapper(fields.get(0), obj, wrapper));
        } else if (fields.size() > 1) {
            queryWrapper.nested(wrapper -> fields.forEach(field -> createWrapper(field, obj, wrapper.or())));
        }
        //排序字段
        filterSortColumns(clazz, obj)
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
        QueryColumn column = field.getAnnotation(QueryColumn.class);
        SqlKeyword keyword = column.word();
        Object value = getValue(field, obj);
        String[] name = column.dbName();
        if (StringUtils.isAllBlank(name)) {
            String columnName = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(field.getName());
            page.add(columnName, value, keyword, wrapper);
        } else {
            boolean isOr = field.isAnnotationPresent(OrStatement.class);
            if (isOr) {
                page.orMulti(wrapper, keyword, value, name);
            } else {
                Arrays.asList(name).forEach(columnName ->
                        page.add(columnName, value, keyword, wrapper));
            }
        }
    }

    private Object getValue(Field field, Object obj) {
        try {
            return FieldUtils.readField(field, obj, true);
        } catch (IllegalAccessException ex) {
            log.error("获取字段值异常 ====> ", ex);
            return null;
        }
    }

    private List<Field> filterColumns(Class<?> clazz, Object obj, boolean isOr) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, QueryColumn.class);
        OrStatement statement = clazz.getAnnotation(OrStatement.class);
        if (statement == null) {
            return isOr ? new ArrayList<>(0) : fields;
        }
        String[] fieldNames = statement.value();
        return fields.stream().filter(field -> !isOr ^ checkFieldInOr(field, fieldNames))
                .filter(field -> !field.getAnnotation(QueryColumn.class).sort())
                .filter(field -> Objects.nonNull(getValue(field, obj)))
                .collect(Collectors.toList());
    }

    private List<Field> filterSortColumns(Class<?> clazz, Object obj) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, QueryColumn.class);
        return fields.stream().filter(field -> field.getAnnotation(QueryColumn.class).sort())
                .filter(field -> Objects.nonNull(getValue(field, obj)))
                .collect(Collectors.toList());
    }

    private boolean checkFieldInOr(Field field, String[] fieldNames) {
        return StringUtils.isAllBlank(fieldNames)
                || Arrays.asList(fieldNames).contains(field.getName());
    }
}
