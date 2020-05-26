package com.unbright.query.extension.constant;

import com.unbright.query.extension.support.SqlExpression;
import lombok.Getter;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/5/26
 * Time: 11:12
 *
 * @author WZP
 * @version v1.0
 */
@Getter
public enum SqlFunction implements SqlExpression {

    COUNT("count(%s)"),

    SUM("sum(%s)");

    private final String sql;

    SqlFunction(String sql) {
        this.sql = sql;
    }

    @Override
    public String parse(Object... args) {
        return String.format(this.sql, args);
    }
}
