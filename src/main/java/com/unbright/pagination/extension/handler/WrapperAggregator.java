package com.unbright.pagination.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:48
 *
 * @author WZP
 * @version v1.0
 */
@Setter
@AllArgsConstructor
public class WrapperAggregator<T> {

    private WrapperAggregateStrategy<T> aggregateStrategy;

    public void aggregate(WrapperBuilder<T> builder, QueryWrapper<T> queryWrapper, Object obj) {
        aggregateStrategy.aggregate(builder, queryWrapper, obj);
    }
}
