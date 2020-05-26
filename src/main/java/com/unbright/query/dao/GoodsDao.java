package com.unbright.query.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unbright.query.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/21
 * Time: 15:16
 *
 * @author WZP
 * @version v1.0
 */
@Mapper
@Repository
public interface GoodsDao extends BaseMapper<Goods> {
}
