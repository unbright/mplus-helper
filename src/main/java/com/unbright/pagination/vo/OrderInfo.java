package com.unbright.pagination.vo;

import com.unbright.pagination.entity.Goods;
import com.unbright.pagination.entity.Order;
import com.unbright.pagination.entity.User;
import com.unbright.pagination.extension.annotation.Alias;
import com.unbright.pagination.extension.annotation.SqlTemplate;
import com.unbright.pagination.extension.constant.SqlFunction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/22
 * Time: 14:58
 *
 * @author WZP
 * @version v1.0
 */
@Data
public class OrderInfo {

    @Alias(entity = Order.class)
    private String id;

    @SqlTemplate(function = SqlFunction.SUM)
    @Alias(entity = Order.class)
    private BigDecimal totalPrice;

    @Alias(entity = Order.class)
    private LocalDateTime createTime;

    @Alias(entity = Order.class)
    private int number;

    @Alias(name = "name", entity = User.class)
    private String username;

    @Alias(name = "name", entity = Goods.class)
    private String goodsName;

    @Alias(entity = Goods.class)
    private int stock;
}
