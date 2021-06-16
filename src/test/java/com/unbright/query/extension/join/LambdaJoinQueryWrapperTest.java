package com.unbright.query.extension.join;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unbright.query.entity.Goods;
import com.unbright.query.entity.Order;
import com.unbright.query.entity.User;
import com.unbright.query.extension.join.query.JoinQueryWrapper;
import com.unbright.query.vo.OrderInfo;
import org.assertj.core.api.Assertions;
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
                .eq(user::getId, 1)
                .ge(order::getTotalPrice, BigDecimal.valueOf(1))
                .orderByDesc(order::getCreateTime)
                .result(OrderInfo.class);
        List<OrderInfo> dtos = complexQuery.selectList(wrapper);
        System.out.println(dtos);
        Assertions.assertThat(dtos).size().isEqualTo(2);
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
                .eq(user::getId, 1)
                .ge(order::getTotalPrice, BigDecimal.valueOf(1))
                .groupBy(user::getId)
                .orderByDesc(order::getCreateTime)
                .limit(1).offset(1)
                .result(OrderInfo.class);
        IPage<OrderInfo> page = complexQuery.selectPage(wrapper);
        System.out.println(new ObjectMapper().writeValueAsString(page));
    }
}
