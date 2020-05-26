package com.unbright.query.extension.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unbright.query.extension.QueryPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 11:46
 *
 * @author WZP
 * @version v1.0
 */
@RequiredArgsConstructor
public class QueryPageWrapper<T> {

    @Getter
    private Object target;
    @Getter
    private QueryPage<T> page;

    private QueryWrapper<T> queryWrapper;
    private WrapperBuilder<T> builder;

    public QueryPageWrapper(QueryPage<T> page) {
        this(null, page);
    }

    public QueryPageWrapper(Object target, QueryPage<T> page) {
        this.target = target;
        this.page = page;
        this.queryWrapper = new QueryWrapper<>();
        this.builder = new DefaultWrapperBuilder<>(this.page);
    }

    /**
     * 构造查询信息.
     */
    public void createQuery() {
        //先聚合and条件
        WrapperAggregator<T> aggregator = new WrapperAggregator<>(new WrapperAndsAggregateStrategy<>());
        aggregator.aggregate(this.builder, this.queryWrapper, this.target);
        //聚合or的条件
        aggregator.setAggregateStrategy(new WrapperOrsAggregateStrategy<>());
        aggregator.aggregate(this.builder, this.queryWrapper, this.target);
        //聚合排序字段
        aggregator.setAggregateStrategy(new WrapperSortsAggregateStrategy<>());
        aggregator.aggregate(this.builder, this.queryWrapper, this.target);
        this.page.setWrapper(queryWrapper);
    }

    public void reset(Object target) {
        this.target = target;
        this.page.clear(this.queryWrapper);
        this.createQuery();
    }
}
