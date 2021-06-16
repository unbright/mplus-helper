package com.unbright.query.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unbright.query.entity.User;
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
@Repository
public interface UserDao extends BaseMapper<User> {
}
