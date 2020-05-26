package com.unbright.query.extension.join.query;

import java.lang.reflect.Field;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/5/26
 * Time: 14:06
 *
 * @author WZP
 * @version v1.0
 */
public interface SelectQueryTransfer {

    String transform(Field field);

}
