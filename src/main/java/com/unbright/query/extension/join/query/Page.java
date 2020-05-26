package com.unbright.query.extension.join.query;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/24
 * Time: 16:39
 *
 * @author WZP
 * @version v1.0
 */
public interface Page<Children> {

    Children limit(long limit);

    Children offset(long offset);
}
