package com.unbright.pagination.extension.annotation;

import com.unbright.pagination.extension.constant.SqlFunction;
import com.unbright.pagination.extension.support.SqlExpression;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/5/26
 * Time: 11:23
 *
 * @author WZP
 * @version v1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SqlTemplate {

    SqlFunction function();

}
