package com.unbright.pagination.extension.join.query;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 15:01
 *
 * @author WZP
 * @version v1.0
 */
public interface Alias<Children> {

    Children as(String alias);

}
