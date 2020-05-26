package com.unbright.query.vo;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.unbright.query.extension.annotation.OrStatement;
import com.unbright.query.extension.annotation.QueryColumn;
import lombok.Data;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/15
 * Time: 15:24
 * <p>
 * 如下例子的SQL语句为 where  (t1.nickname LIKE ? OR t2.nickname LIKE ?) AND (age >= ? OR t1.type = ?) ORDER BY create_time DESC
 * </p>
 *
 * @author WZP
 * @version v1.0
 */
@Data
@OrStatement({"age", "type"})
public class QueryCondition {

    /**
     * 声明多个or联合查询.
     * <p>
     * t1.nickname like ? or t2.nickname like ?
     * </p>
     */
    @OrStatement
    @QueryColumn(dbName = {"t1.nickname", "t2.nickname"}, word = SqlKeyword.LIKE)
    private String name;

    /**
     * 等于查询 and.
     * <p>
     * age = ?
     * </p>
     */
    @QueryColumn(word = SqlKeyword.GE)
    private Integer age;

    /**
     * 自定义字段名字查询 and.
     * t1.type = ?
     */
    @QueryColumn(dbName = "t1.type")
    private String type;

    /**
     * 声明排序字段.
     * order by ? desc
     */
    @QueryColumn(word = SqlKeyword.DESC, sort = true)
    private String createTime;
}
