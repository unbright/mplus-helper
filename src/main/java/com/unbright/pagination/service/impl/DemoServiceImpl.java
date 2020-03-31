package com.unbright.pagination.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unbright.pagination.dao.DemoDao;
import com.unbright.pagination.entity.Haphazard;
import com.unbright.pagination.extension.QueryPage;
import com.unbright.pagination.service.DemoService;
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
public class DemoServiceImpl extends ServiceImpl<DemoDao, Haphazard> implements DemoService {

    @Override
    public IPage customQuery(QueryPage page) {
        return baseMapper.queryPage(page);
    }
}
