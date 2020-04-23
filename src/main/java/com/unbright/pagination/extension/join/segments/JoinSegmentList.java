package com.unbright.pagination.extension.join.segments;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.segments.AbstractISegmentList;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 16:23
 *
 * @author WZP
 * @version v1.0
 */
public class JoinSegmentList extends AbstractISegmentList {
    @Override
    protected boolean transformList(List<ISqlSegment> list, ISqlSegment firstSegment, ISqlSegment lastSegment) {
        final String sql = list.stream().map(ISqlSegment::getSqlSegment).collect(joining(SPACE));
        list.clear();
        list.add(() -> sql);
        return true;
    }

    @Override
    protected String childrenSqlSegment() {
        if (isEmpty()) {
            return EMPTY;
        }
        return this.stream().map(ISqlSegment::getSqlSegment).collect(joining(SPACE, EMPTY, SPACE));
    }
}
