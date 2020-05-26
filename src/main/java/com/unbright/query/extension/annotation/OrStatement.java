package com.unbright.query.extension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/15
 * Time: 8:32
 * <p>
 * 声明or条件
 * </p>
 *
 * @author WZP
 * @version v1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface OrStatement {

    /**
     * 为or的字段
     *
     * @return or字段，会聚合到括号里
     */
    String[] value() default {};
}
