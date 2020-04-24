package com.unbright.pagination.extension.join;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbright.pagination.entity.Goods;
import com.unbright.pagination.entity.Order;
import com.unbright.pagination.entity.User;
import com.unbright.pagination.extension.join.query.JoinQueryWrapper;
import com.unbright.pagination.vo.OrderInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/17
 * Time: 15:39
 *
 * @author WZP
 * @version v1.0
 */
@SpringBootTest
class LambdaJoinQueryWrapperTest {

    @Autowired
    private ComplexQuery complexQuery;

    @Test
    public void testQuery() {
        Order order = new Order();
        User user = new User();
        Goods goods = new Goods();
        JoinQueryWrapper wrapper = JoinWrappers.select(order.getClass()).as("od").join(user.getClass()).as("u")
                .on(user::getId, order::getUserId)
                .join(goods.getClass()).as("g")
                .on(goods::getId, order::getGoodsId)
                .eq(user::getId, 2)
                .ge(order::getTotalPrice, BigDecimal.valueOf(1))
                .orderByDesc(order::getCreateTime)
                .result(OrderInfo.class);
        List<OrderInfo> dtos = complexQuery.selectList(wrapper);
        System.out.println(dtos);
    }

    @Test
    public void testQueryPage() throws JsonProcessingException {
        Order order = new Order();
        User user = new User();
        Goods goods = new Goods();
        JoinQueryWrapper wrapper = JoinWrappers.select(order.getClass()).as("od").join(user.getClass()).as("u")
                .on(user::getId, order::getUserId)
                .join(goods.getClass()).as("g")
                .on(goods::getId, order::getGoodsId)
                .eq(user::getId, 3)
                .ge(order::getTotalPrice, BigDecimal.valueOf(1))
                .orderByDesc(order::getCreateTime)
                .limit(1).offset(3)
                .result(OrderInfo.class);
        IPage<OrderInfo> page = complexQuery.selectPage(wrapper);
        System.out.println(new ObjectMapper().writeValueAsString(page));
    }
}
