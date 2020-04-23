package com.unbright.pagination.extension.join.query;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.unbright.pagination.extension.annotation.Alias;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.COMMA;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.EMPTY;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.SPACE;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 15:24
 *
 * @author WZP
 * @version v1.0
 */
public class JoinQueryWrapper extends AbstractJoinQueryWrapper<JoinQueryWrapper> {

    private SharedString sqlSelect = new SharedString();
    private Class<?> result;

    @SuppressWarnings("unchecked")
    public JoinQueryWrapper from(Class<?> entity) {
        super.setEntityClass(entity);
        return typedThis;
    }

    public JoinQueryWrapper result(Class<?> dto) {
        this.result = dto;
        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(dto, Alias.class);
        String selectStr = fields.stream().map(this::getSelectColumn).collect(Collectors.joining(COMMA, EMPTY, SPACE));
        this.sqlSelect.setStringValue(selectStr);
        return typedThis;
    }

    private String getSelectColumn(Field field) {
        Alias alias = field.getAnnotation(Alias.class);
        String name = alias.name();
        //c.id as name
        if (StringUtils.isBlank(name)) {
            name = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(field.getName());
        }
        return String.format("%s.%s AS %s", getTableAlias(alias.entity()), name, field.getName());
    }

    public String getFromTable() {
        // table_name AS c
        Class<?> clazz = getEntityClass();
        return String.format("%s AS %s", getTableName(clazz), getTableAlias(clazz));
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }

    public Class<?> getResultClass() {
        return this.result;
    }
}
