package com.unbright.query.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unbright.query.dao.OrderDao;
import com.unbright.query.entity.Order;
import com.unbright.query.extension.QueryPage;
import com.unbright.query.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/31
 * Time: 15:27
 *
 * @author WZP
 * @version v1.0
 */
@Service
public class DemoServiceImpl extends ServiceImpl<OrderDao, Order> implements DemoService {

    @Override
    public IPage customQuery(QueryPage page) {
        return baseMapper.queryPage(page);
    }
}
