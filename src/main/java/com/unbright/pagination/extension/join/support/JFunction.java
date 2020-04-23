package com.unbright.pagination.extension.join.support;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/21
 * Time: 11:53
 *
 * @author WZP
 * @version v1.0
 */
@FunctionalInterface
public interface JFunction extends SFunction<Object, Object>, Supplier<Object>, Serializable {

    @Override
    default Object apply(Object o) {
        return o;
    }
}
