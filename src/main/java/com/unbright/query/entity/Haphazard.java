package com.unbright.query.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/31
 * Time: 15:26
 *
 * @author WZP
 * @version v1.0
 */
@Data
public class Haphazard {

    private String id;

    private String name;

    private String categoryId;

    private String description;

    private LocalDateTime createTime;
}
