package com.unbright.pagination.vo;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.unbright.pagination.extension.annotation.QueryColumn;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/24
 * Time: 17:04
 *
 * @author WZP
 * @version v1.0
 */
@Data
public class OrderQueryVo {

    @QueryColumn(dbName = "u.id")
    private String userId;

    @QueryColumn(dbName = "od.total_price", word = SqlKeyword.GE)
    private BigDecimal totalPrice;
}
