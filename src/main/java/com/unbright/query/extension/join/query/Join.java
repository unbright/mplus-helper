package com.unbright.query.extension.join.query;

import java.io.Serializable;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 14:48
 *
 * @author WZP
 * @version v1.0
 */
public interface Join<Children, R1, R2> extends Serializable {

    Children join(Class<?> table);

    Children leftJoin(Class<?> table);

    Children rightJoin(Class<?> table);

    Children on(R1 column1, R2 column2);
}
