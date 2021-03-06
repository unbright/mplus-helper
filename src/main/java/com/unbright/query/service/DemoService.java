package com.unbright.query.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.unbright.query.entity.Order;
import com.unbright.query.extension.QueryPage;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/31
 * Time: 15:26
 *
 * @author WZP
 * @version v1.0
 */
public interface DemoService extends IService<Order> {

    IPage customQuery(QueryPage page);
}
