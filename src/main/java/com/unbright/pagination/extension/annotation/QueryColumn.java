package com.unbright.pagination.extension.annotation;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/15
 * Time: 8:35
 * <p>
 * 声明查询字段.
 * </p>
 *
 * @author WZP
 * @version v1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryColumn {

    /**
     * sql语句中的名字，为空则默认驼峰转下划线.
     *
     * @return 字段名字
     */
    String[] dbName() default {};

    /**
     * 查询条件.
     *
     * @return sql中的条件，默认为 =
     */
    SqlKeyword word() default SqlKeyword.EQ;

    /**
     * 是否排序字段.
     *
     * @return true/false
     */
    boolean sort() default false;
}
