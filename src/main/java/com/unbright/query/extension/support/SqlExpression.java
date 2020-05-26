package com.unbright.query.extension.support;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/5/26
 * Time: 11:16
 *
 * @author WZP
 * @version v1.0
 */
public interface SqlExpression {

    String parse(Object... args);
}
