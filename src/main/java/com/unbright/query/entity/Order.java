package com.unbright.query.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/23
 * Time: 8:47
 *
 * @author WZP
 * @version v1.0
 */
@Data
@TableName("t_order")
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private int userId;
    private int goodsId;
    private BigDecimal price;
    private int number;
    private BigDecimal totalPrice;
}
