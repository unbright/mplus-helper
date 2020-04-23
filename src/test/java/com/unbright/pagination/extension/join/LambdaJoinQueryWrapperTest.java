package com.unbright.pagination.extension.join;

import com.unbright.pagination.entity.Goods;
import com.unbright.pagination.entity.Order;
import com.unbright.pagination.entity.User;
import com.unbright.pagination.extension.join.query.JoinQueryWrapper;
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
        //wrapper.join(FarmUnit.class, FarmUser.class).on(FarmUnit::getId,FarmUser::getId)
        //wrapper.from(FarmUnit.class).join(FarmUser.class).on(FarmUnit::getId,FarmUser::getId)
        //.join(FarmD.class).on()
        //.select(result.class).join("user").as("c").on("a.id","b.id")
        //.join("unit").on("c.id","d.id")
        //.eq("a.id",12).eq("c.id","30")
        List<OrderInfo> dtos = complexQuery.selectList(wrapper);
        System.out.println(dtos);
    }
}
