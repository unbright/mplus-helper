package com.unbright.query.extension.join.query;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.unbright.query.extension.join.segments.JoinOnMergeSegment;
import com.unbright.query.extension.join.segments.JoinSegment;
import com.unbright.query.extension.support.JFunction;
import com.unbright.query.extension.util.FunctionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.ASC;
import static com.baomidou.mybatisplus.core.enums.SqlKeyword.DESC;
import static com.baomidou.mybatisplus.core.enums.SqlKeyword.GROUP_BY;
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
public abstract class AbstractJoinWrapper<Children extends AbstractJoinWrapper<Children>> extends LambdaQueryWrapper
        implements Join<Children, JFunction, JFunction>, Alias<Children>, Page<Children>, SelectQueryTransfer {

    protected final Children typedThis = (Children) this;

    protected final Map<String, String> joinMap = new ConcurrentHashMap<>(1);
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
        List<ISqlSegment> sqlSegments = new ArrayList<>();
        sqlSegments.add(JoinSegment.JOIN);
        sqlSegments.add(this.joinString::getStringValue);
        String alias = this.joinMap.get(this.currentJoin.getStringValue());
        if (StringUtils.isNotBlank(alias)) {
            sqlSegments.add(JoinSegment.AS);
            sqlSegments.add(() -> this.joinMap.get(this.currentJoin.getStringValue()));
        }
        sqlSegments.add(JoinSegment.ON);
        sqlSegments.add(() -> getQueryColumn(column1));
        sqlSegments.add(() -> StringPool.EQUALS);
        sqlSegments.add(() -> getQueryColumn(column2));
        this.segment.add(sqlSegments.toArray(new ISqlSegment[0]));
        return typedThis;
    }

    protected String getQueryColumn(JFunction fn) {
        String cache = columnCache.get(fn);
        if (StringUtils.isBlank(cache)) {
            SerializedLambda lambda = FunctionUtils.resolve(fn);
            TableInfo tableInfo = TableInfoHelper.getTableInfo(FunctionUtils.getInstantiatedClass(fn));
            String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
            String alias = joinMap.get(tableInfo.getTableName());
            if (StringUtils.isBlank(alias)) {
                alias = tableInfo.getTableName();
            }
            String column = alias + "." + com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName);
            columnCache.put(fn, column);
            return column;
        }
        return cache;
    }

    private SerializedLambda serialized(Object lambda) {
        try {
            Method writeMethod = lambda.getClass().getDeclaredMethod("writeReplace");
            writeMethod.setAccessible(true);
            return (SerializedLambda) writeMethod.invoke(lambda);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getQueryColumns(JFunction... fns) {
        return Stream.of(fns).map(this::getQueryColumn).collect(Collectors.joining(","));
    }

    protected Children addCondition(boolean condition, JFunction column, SqlKeyword sqlKeyword, Object val) {
        this.maybeDo(condition, () -> appendSqlSegments(() -> getQueryColumn(column), sqlKeyword, () -> formatParam(null, val)));
        return typedThis;
    }

    public Children eq(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.EQ, val);
    }

    public Children ne(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.NE, val);
    }

    public Object in(Object column, Object... values) {
        return this.addCondition(true, column, SqlKeyword.IN, values);
    }

    public Object like(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.LIKE, val);
    }

    public Children ge(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.GE, val);
    }

    public Children le(JFunction column, Object val) {
        return this.addCondition(true, column, SqlKeyword.LE, val);
    }

    public Children orderByDesc(JFunction... columns) {
        return this.orderBy(true, false, columns);
    }

    public Children orderByAsc(JFunction... columns) {
        return this.orderBy(true, true, columns);
    }

    public Children orderBy(boolean condition, boolean isAsc, JFunction... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return typedThis;
        }
        SqlKeyword mode = isAsc ? ASC : DESC;
        this.maybeDo(condition, () -> Arrays.stream(columns).forEach(c -> appendSqlSegments(ORDER_BY,
                () -> this.getQueryColumn(c), mode)));
        return typedThis;
    }

    public Children groupBy(JFunction... columns) {
        return this.groupBy(true, columns);
    }

    public Children groupBy(boolean condition, JFunction... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.maybeDo(condition, () -> appendSqlSegments(GROUP_BY, () -> this.getQueryColumns(columns)));
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

    protected String getTableName(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        return tableInfo.getTableName();
    }

    protected String getTableAlias(Class<?> clazz) {
        String alias = joinMap.get(getTableName(clazz));
        if (StringUtils.isBlank(alias)) {
            return getTableName(clazz);
        }
        return alias;
    }
}
