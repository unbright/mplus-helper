package com.unbright.pagination.extension.join.query;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.unbright.pagination.extension.util.FunctionUtils;
import com.unbright.pagination.extension.join.support.JFunction;
import com.unbright.pagination.extension.join.segments.JoinOnMergeSegment;
import com.unbright.pagination.extension.join.segments.JoinSegment;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.ASC;
import static com.baomidou.mybatisplus.core.enums.SqlKeyword.DESC;
import static com.baomidou.mybatisplus.core.enums.SqlKeyword.ORDER_BY;


/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 15:02
 *
 * @author WZP
 * @version v1.0
 */
@SuppressWarnings("all")
public abstract class AbstractJoinQueryWrapper<Children extends AbstractJoinQueryWrapper<Children>> extends LambdaQueryWrapper
        implements Join<Children, JFunction, JFunction>, Alias<Children> {

    /**
     * 占位符
     */
    protected final Children typedThis = (Children) this;

    protected final Map<String, String> joinMap = new ConcurrentHashMap<>(1);
    protected final Map<String, String> tableMap = new ConcurrentHashMap<>(1);
    private final Map<JFunction, String> columnCache = new ConcurrentHashMap<>(1);

    private final JoinOnMergeSegment segment = new JoinOnMergeSegment();
    private final SharedString joinString = new SharedString();
    private final SharedString currentJoin = new SharedString();

    @Override
    public Children join(Class<?> table) {
        //join table
        TableInfo tableInfo = TableInfoHelper.getTableInfo(table);
        this.joinString.setStringValue(tableInfo.getTableName());
        this.currentJoin.setStringValue(tableInfo.getTableName());
        return typedThis;
    }

    @Override
    public Children leftJoin(Class<?> table) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(table);
        this.joinString.setStringValue(tableInfo.getTableName());
        return typedThis;
    }

    @Override
    public Children rightJoin(Class<?> table) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(table);
        this.joinString.setStringValue(tableInfo.getTableName());
        return typedThis;
    }

    @Override
    public Children on(JFunction column1, JFunction column2) {
        this.segment.add(JoinSegment.JOIN, this.joinString::getStringValue,
                JoinSegment.AS, () -> this.joinMap.get(this.currentJoin.getStringValue()),
                JoinSegment.ON, () -> getQueryColumn(column1), () -> StringPool.EQUALS,
                () -> getQueryColumn(column2));
        return typedThis;
    }

    protected String getQueryColumn(JFunction fn) {
        String cache = columnCache.get(fn);
        if (StringUtils.isBlank(cache)) {
            SerializedLambda lambda = FunctionUtils.resolve(fn);
            TableInfo tableInfo = TableInfoHelper.getTableInfo(lambda.getImplClass());
            String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
            String column = joinMap.get(tableInfo.getTableName()) + "." + com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName);
            columnCache.put(fn, column);
            return column;
        }
        return cache;
    }

    protected Children addCondition(boolean condition, JFunction column, SqlKeyword sqlKeyword, Object val) {
        this.doIt(condition, () -> getQueryColumn(column), sqlKeyword, () -> this.formatSql("{0}", val));
        return typedThis;
    }

    public Children eq(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.EQ, val);
    }

    public Children ge(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.GE, val);
    }

    public Children orderByDesc(JFunction... columns) {
        return this.orderBy(true, false, columns);
    }

    public Children orderBy(boolean condition, boolean isAsc, JFunction... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return typedThis;
        }
        SqlKeyword mode = isAsc ? ASC : DESC;
        for (JFunction column : columns) {
            doIt(condition, ORDER_BY, () -> getQueryColumn(column), mode);
        }
        return typedThis;
    }

    @Override
    public String getSqlSegment() {
        return this.segment.getSqlSegment() + super.getSqlSegment();
    }

    @Override
    public Children as(String alias) {
        String currentJoin = this.currentJoin.getStringValue();
        if (StringUtils.isBlank(currentJoin)) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(getEntityClass());
            joinMap.put(tableInfo.getTableName(), alias);
        } else {
            joinMap.put(currentJoin, alias);
        }
        return typedThis;
    }

    @Override
    public String getSqlFirst() {
        String sqlFirst = super.getSqlFirst();
        if (StringUtils.isBlank(sqlFirst)) {
            return "";
        }
        return sqlFirst;
    }

    @Override
    public String getSqlComment() {
        String comment = super.getSqlComment();
        if (StringUtils.isBlank(comment)) {
            return "";
        }
        return comment;
    }

    protected String getTableName(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        return tableInfo.getTableName();
    }

    protected String getTableAlias(Class<?> clazz) {
        return joinMap.get(getTableName(clazz));
    }
}
