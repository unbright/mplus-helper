package com.unbright.query.extension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/21
 * Time: 10:09
 * <p>
 * 声明查询别名.
 *
 * @author WZP
 * @version v1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Alias {

    /**
     * 查询字段的名称,不传则默认驼峰转下划线小写.
     */
    String name() default "";

    /**
     * 所属实体.
     */
    Class<?> entity();
}
