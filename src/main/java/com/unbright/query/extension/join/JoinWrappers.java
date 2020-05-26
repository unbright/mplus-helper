package com.unbright.query.extension.join;

import com.unbright.query.extension.join.query.JoinQueryWrapper;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/23
 * Time: 10:58
 *
 * @author WZP
 * @version v1.0
 */
public final class JoinWrappers {

    public static JoinQueryWrapper query() {
        return new JoinQueryWrapper();
    }

    public static JoinQueryWrapper select(Class<?> clazz) {
        return query().from(clazz);
    }
}
