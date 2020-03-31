package com.unbright.pagination.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.unbright.pagination.entity.Haphazard;
import com.unbright.pagination.extension.QueryPage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/31
 * Time: 15:23
 *
 * @author WZP
 * @version v1.0
 */
@Mapper
@Repository
public interface DemoDao extends BaseMapper<Haphazard> {

    IPage queryPage(QueryPage page);
}
