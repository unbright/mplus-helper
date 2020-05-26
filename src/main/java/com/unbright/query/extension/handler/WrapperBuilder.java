package com.unbright.query.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:19
 * <p>
 * 构造字段查询条件.
 *
 * @author WZP
 * @version v1.0
 */
public interface WrapperBuilder<T> {

    /**
     * 构造查询条件.
     *
     * @param field   字段
     * @param obj     对象
     * @param wrapper 查询构造器.
     */
    void build(Field field, Object obj, QueryWrapper<T> wrapper);

}
