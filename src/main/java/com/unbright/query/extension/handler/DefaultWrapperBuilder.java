package com.unbright.query.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.unbright.query.extension.QueryPage;
import com.unbright.query.extension.annotation.OrStatement;
import com.unbright.query.extension.annotation.QueryColumn;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:43
 *
 * @author WZP
 * @version v1.0
 */
@RequiredArgsConstructor
public class DefaultWrapperBuilder<T> implements WrapperBuilder<T> {

    private final QueryPage<T> page;

    @Override
    public void build(Field field, Object obj, QueryWrapper<T> wrapper) {
        QueryColumn column = field.getAnnotation(QueryColumn.class);
        SqlKeyword keyword = column.word();
        Object value = ObjectUtil.getValue(field, obj);
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
}
