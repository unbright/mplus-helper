package com.unbright.query.extension.join.segments;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 16:15
 *
 * @author WZP
 * @version v1.0
 */
@SuppressWarnings("serial")
public class JoinOnMergeSegment extends MergeSegments {

    private final JoinSegmentList join = new JoinSegmentList();

    @Override
    public void add(ISqlSegment... sqlSegments) {
        List<ISqlSegment> segments = Arrays.asList(sqlSegments);
        ISqlSegment first = segments.get(0);
        if (JoinSegment.JOIN.match(first)) {
            this.join.addAll(segments);
        }
    }

    @Override
    public String getSqlSegment() {
        return this.join.getSqlSegment()
                + Constants.WHERE + StringPool.SPACE;
    }

    @Override
    public void clear() {
        super.clear();
        this.join.clear();
    }
}
