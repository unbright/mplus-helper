package com.unbright.pagination.extension.join.segments;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;

import java.util.function.Predicate;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 16:19
 *
 * @author WZP
 * @version v1.0
 */
public enum JoinSegment implements ISqlSegment {

    JOIN(key -> key.getSqlSegment().equalsIgnoreCase("join")),

    ON(key -> key.getSqlSegment().equalsIgnoreCase("on")),

    AS(key -> key.getSqlSegment().equalsIgnoreCase("as")),

    LEFT_JOIN(key -> key.getSqlSegment().equalsIgnoreCase("left join")),

    RIGHT_JOIN(key -> key.getSqlSegment().equalsIgnoreCase("left join")),
    ;

    private final Predicate<ISqlSegment> predicate;

    JoinSegment(Predicate<ISqlSegment> predicate) {
        this.predicate = predicate;
    }

    public boolean match(ISqlSegment segment) {
        return this.getPredicate().test(segment);
    }

    protected Predicate<ISqlSegment> getPredicate() {
        return this.predicate;
    }

    @Override
    public String getSqlSegment() {
        return this.name();
    }
}
