package com.unbright.query.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/23
 * Time: 8:45
 *
 * @author WZP
 * @version v1.0
 */
@Data
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
