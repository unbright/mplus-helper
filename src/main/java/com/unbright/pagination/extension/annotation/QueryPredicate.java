package com.unbright.pagination.extension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/16
 * Time: 9:43
 *
 * <p>
 * 查询对象解析
 * </p>
 *
 * @author WZP
 * @version v1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface QueryPredicate {

    /**
     * 如果不传，则默认使用request中的请求参数为查询条件.
     */
    Class<?> value() default void.class;
}
