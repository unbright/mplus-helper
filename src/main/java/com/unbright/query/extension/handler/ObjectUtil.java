package com.unbright.query.extension.handler;

import com.unbright.query.extension.annotation.OrStatement;
import com.unbright.query.extension.annotation.QueryColumn;
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
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:33
 *
 * @author WZP
 * @version v1.0
 */
@Slf4j
class ObjectUtil {

    static List<Field> filterColumns(Class<?> clazz, Object obj, boolean isOr) {
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

    static List<Field> filterSortColumns(Class<?> clazz, Object obj) {
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(clazz, QueryColumn.class);
        return fields.stream().filter(field -> field.getAnnotation(QueryColumn.class).sort())
                .filter(field -> Objects.nonNull(getValue(field, obj)))
                .collect(Collectors.toList());
    }

    static boolean checkFieldInOr(Field field, String[] fieldNames) {
        return StringUtils.isAllBlank(fieldNames)
                || Arrays.asList(fieldNames).contains(field.getName());
    }

    static Object getValue(Field field, Object obj) {
        try {
            return FieldUtils.readField(field, obj, true);
        } catch (IllegalAccessException ex) {
            log.error("获取字段值异常 ====> ", ex);
            return null;
        }
    }
}
