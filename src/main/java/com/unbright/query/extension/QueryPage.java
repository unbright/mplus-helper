package com.unbright.query.extension;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/30
 * Time: 11:13
 *
 * @author wzpeng
 * @version v1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(value = {"params", "wrapper", "ew"})
public class QueryPage<T> extends Page<T> {

    private static final String SEPARATOR = "|";

    private Map<String, String> params;
    private QueryWrapper<T> wrapper;

    public QueryPage() {
        super(0, 10);
    }

    public QueryPage(long current, long size) {
        super(current, size);
        wrapper = new QueryWrapper<>();
    }

    public QueryPage(long current, long size, Map<String, String> params) {
        this(current, size);
        this.params = params;
        params.entrySet().parallelStream().forEach(e -> wrapper.eq(String.valueOf(e.getKey()), e.getValue()));
    }

    /**
     * 添加like条件.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addLike(String column, Object value) {
        return this.addLike(column, value, this.wrapper);
    }

    /**
     * 添加like条件.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addLike(String column, Object value, QueryWrapper<T> wrapper) {
        if (value != null) {
            wrapper.like(column, value);
        }
        this.setWrapper(wrapper);
        return this;
    }

    /**
     * use lambda.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addLike(SFunction<T, Object> column, Object value) {
        return addLike(getDbColumn(column), value);
    }

    /**
     * greater than equals.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addGte(String column, Object value) {
        return this.addGte(column, value, this.wrapper);
    }

    /**
     * greater than equals.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addGte(String column, Object value, QueryWrapper<T> wrapper) {
        if (value != null) {
            wrapper.ge(column, value);
        }
        this.setWrapper(wrapper);
        return this;
    }

    /**
     * use lambda.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addGte(SFunction<T, Object> column, Object value) {
        return addGte(getDbColumn(column), value);
    }

    /**
     * less than equals.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addLte(String column, Object value) {
        return this.addLte(column, value, wrapper);
    }

    /**
     * less than equals.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addLte(String column, Object value, QueryWrapper<T> wrapper) {
        if (value != null) {
            wrapper.le(column, value);
        }
        this.setWrapper(wrapper);
        return this;
    }

    /**
     * equals.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addEq(String column, Object value) {
        return this.addEq(column, value, this.wrapper);
    }

    /**
     * equals.
     *
     * @param column 字段
     * @param value  值
     * @return current
     */
    public QueryPage<T> addEq(String column, Object value, QueryWrapper<T> wrapper) {
        if (value != null) {
            wrapper.eq(column, value);
        }
        this.setWrapper(wrapper);
        return this;
    }

    /**
     * lambda
     */
    public QueryPage<T> addEq(SFunction<T, Object> column, Object value) {
        return addEq(getDbColumn(column), value);
    }

    /**
     * not equals
     */
    public QueryPage<T> addNe(String column, Object value) {
        if (value != null) {
            this.wrapper.ne(column, value);
        }
        return this;
    }

    public QueryPage<T> addIn(String column, Object... value) {
        if (value != null) {
            this.wrapper.in(column, value);
        }
        return this;
    }

    public QueryPage<T> addIn(SFunction<T, Object> function, Object... value) {
        return addIn(getDbColumn(function), value);
    }

    /**
     * 添加条件.
     *
     * @param column  字段
     * @param value   值
     * @param keyword 条件
     * @return current
     */
    public QueryPage<T> add(String column, Object value, SqlKeyword keyword) {
        return this.add(column, value, keyword, this.wrapper);
    }

    /**
     * 添加条件.
     *
     * @param column  字段
     * @param value   值
     * @param keyword 条件
     * @return current
     */
    public QueryPage<T> add(String column, Object value, SqlKeyword keyword, QueryWrapper<T> wrapper) {
        switch (keyword) {
            case EQ:
                return addEq(column, value, wrapper);
            case LIKE:
                return addLike(column, value, wrapper);
            case GE:
                return addGte(column, value, wrapper);
            case LE:
                return addLte(column, value, wrapper);
            case ASC:
                return addOrderByAsc(wrapper, String.valueOf(value));
            case DESC:
                return addOrderByDesc(wrapper, String.valueOf(value));
        }
        return this;
    }

    /**
     * and (a = ? or b = ?)
     */
    public QueryPage<T> orEqMulti(Object value, String... columns) {
        if (ObjectUtils.isNotEmpty(value)) {
            this.wrapper.and(queryWrapper -> Arrays.asList(columns).forEach(key -> queryWrapper.or().eq(key, value)));
        }
        return this;
    }

    /**
     * and (a like ? or b like ?)
     */
    public QueryPage<T> orLikeMulti(Object value, String... columns) {
        if (ObjectUtils.isNotEmpty(value)) {
            this.wrapper.and(queryWrapper -> Arrays.asList(columns).forEach(key -> queryWrapper.or().like(key, value)));
        }
        return this;
    }

    /**
     * custom condition
     */
    public QueryPage<T> orMulti(QueryWrapper<T> wrapper, SqlKeyword keyword, Object value, String... columns) {
        if (ObjectUtils.isNotEmpty(value)) {
            wrapper.and(queryWrapper ->
                    Arrays.asList(columns).forEach(key ->
                            this.add(key, value, keyword, queryWrapper.or())));
        }
        this.setWrapper(wrapper);
        return this;
    }

    @SafeVarargs
    public final QueryPage<T> orLikeMulti(Object value, SFunction<T, Object>... functions) {
        if (ObjectUtils.isNotEmpty(value)) {
            String[] keys = Stream.of(functions).map(this::getDbColumn).toArray(String[]::new);
            this.wrapper.and(queryWrapper -> Arrays.asList(keys).forEach(key -> queryWrapper.or().like(key, value)));
        }
        return this;
    }

    public QueryPage<T> groupBy(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            String[] array = Arrays.stream(columns).skip(1).toArray(String[]::new);
            this.wrapper.groupBy(columns[0], array);
        }
        return this;
    }

    public QueryPage<T> addOrderByAsc(String... keys) {
        return this.addOrderByAsc(this.wrapper, keys);
    }

    public QueryPage<T> addOrderByAsc(QueryWrapper<T> wrapper, String... keys) {
        if (ArrayUtils.isNotEmpty(keys)) {
            String[] array = Arrays.stream(keys).skip(1).toArray(String[]::new);
            wrapper.orderByAsc(keys[0], array);
            this.setWrapper(wrapper);
        }
        return this;
    }

    public QueryPage<T> addOrderByDesc(String... keys) {
        return this.addOrderByDesc(this.wrapper, keys);
    }

    public QueryPage<T> addOrderByDesc(QueryWrapper<T> wrapper, String... keys) {
        if (ArrayUtils.isNotEmpty(keys)) {
            String[] array = Arrays.stream(keys).skip(1).toArray(String[]::new);
            wrapper.orderByDesc(keys[0], array);
            this.setWrapper(wrapper);
        }
        return this;
    }

    @SafeVarargs
    public final QueryPage<T> addOrderByDesc(SFunction<T, Object>... functions) {
        String[] keys = Stream.of(functions).map(this::getDbColumn).toArray(String[]::new);
        return this.addOrderByDesc(keys);
    }

    public QueryPage<T> clear(QueryWrapper<T> wrapper) {
        wrapper.clear();
        return this;
    }

    public QueryPage<T> clear() {
        return this.clear(this.wrapper);
    }

    /**
     * 解析请求参数并转化为筛选条件.
     *
     * @param params 请求参数
     * @return 查询条件
     */
    public QueryPage<T> resolve(Map<String, String> params) {
        params.forEach(this::addCondition);
        return this;
    }

    /**
     * 解析参数并添加筛选条件.
     *
     * @param key   key
     * @param value value
     */
    private void addCondition(String key, String value) {
        if (key.lastIndexOf(SEPARATOR) == -1) {
            this.add(key, value, SqlKeyword.EQ);
        } else {
            String column = key.substring(0, key.lastIndexOf(SEPARATOR));
            String symbol = key.substring(key.lastIndexOf(SEPARATOR) + 1);
            this.add(column, value, SqlKeyword.valueOf(symbol.toUpperCase()));
        }
    }

    public Wrapper<T> getEw() {
        return this.wrapper;
    }

    /**
     * 将方法签名转为数据库字段.
     *
     * @param function function
     * @return column name
     */
    private String getDbColumn(SFunction<T, Object> function) {
        LambdaMeta meta = LambdaUtils.extract(function);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return StringUtils.camelToUnderline(fieldName);
    }
}
