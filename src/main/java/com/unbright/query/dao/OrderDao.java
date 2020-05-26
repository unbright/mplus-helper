package com.unbright.query.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.unbright.query.entity.Order;
import com.unbright.query.extension.QueryPage;
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
public interface OrderDao extends BaseMapper<Order> {

    IPage queryPage(QueryPage page);
}
