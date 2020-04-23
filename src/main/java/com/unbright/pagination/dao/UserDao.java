package com.unbright.pagination.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unbright.pagination.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 15:46
 *
 * @author WZP
 * @version v1.0
 */
@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {
}
