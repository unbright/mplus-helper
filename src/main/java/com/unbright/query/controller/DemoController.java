package com.unbright.query.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unbright.query.entity.Goods;
import com.unbright.query.entity.Haphazard;
import com.unbright.query.entity.Order;
import com.unbright.query.entity.User;
import com.unbright.query.extension.BaseController;
import com.unbright.query.extension.QueryPage;
import com.unbright.query.extension.annotation.QueryPredicate;
import com.unbright.query.extension.handler.QueryPageWrapper;
import com.unbright.query.extension.join.ComplexQuery;
import com.unbright.query.extension.join.JoinWrappers;
import com.unbright.query.extension.join.query.JoinQueryWrapper;
import com.unbright.query.extension.util.QueryUtil;
import com.unbright.query.vo.OrderInfo;
import com.unbright.query.vo.OrderQueryVo;
import com.unbright.query.vo.QueryCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/31
 * Time: 15:27
 *
 * @author WZP
 * @version v1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class DemoController extends BaseController {

    private final ComplexQuery complexQuery;

    /**
     * 单表查询
     */
    @GetMapping("simple")
    public ResponseEntity<Page<Haphazard>> simpleQuery(@RequestParam String categoryId) {
        // ?id=123 => where id = 123
        QueryPage<Haphazard> page = QueryUtil.smartInitPage(request, "categoryId");
        //and category_id = ?
        page.addEq(Haphazard::getCategoryId, categoryId);
        //and name like '%name%'
        page.addLike(Haphazard::getName, "name");
        //and (name like '%name%' or description like '%name%')
        //page.orLikeMulti("name", Haphazard::getName, Haphazard::getDescription);
        //order by create_time desc
        page.addOrderByDesc(Haphazard::getCreateTime);
        System.out.println(page.getWrapper().getCustomSqlSegment());
        return ResponseEntity.ok(page);
    }

    /**
     * 多表join查询.
     * select * from a join b on a.id = b.a_id ${ew.customSqlSegment}
     * ?a.a_name|LIKE=11&b.a_id=12&a.time|DESC=a.time,b.time
     * => where a.a_name like ? and b.a_id = ? order by a.time,b.time desc
     */
    @GetMapping("join")
    public ResponseEntity<IPage> joinQuery(@RequestParam String keywords) {
        QueryPage<?> page = QueryUtil.smartInitPage(request, "keywords");
        //add custom condition
        //and (a.key like ? or b.key like ?)
        page.orLikeMulti(keywords, "a.key", "b.key");
        System.out.println(page.getWrapper().getCustomSqlSegment());
        //demoService.customQuery(smartInitPage());
        return ResponseEntity.ok(page);
    }

    /**
     * 自定义注解复杂查询.
     * <p>
     * 请求地址为: /complex?name=1231&age=10&type=44&createTime=create_time
     * </p>
     *
     * @param condition 条件对象.
     */
    @GetMapping("complex")
    public ResponseEntity<IPage> complexQuery(QueryCondition condition) {
        //自动注入查询条件
        QueryPage<?> page = QueryUtil.smartQuery(request, condition).getPage();
        log.info(page.getEw().getTargetSql());
        //TODO使用mapper查询
        return ResponseEntity.ok(page);
    }

    /**
     * 自定义注解复杂查询.
     * <p>
     * 请求地址为: /complex2?name=1231&age=10&type=44&createTime=create_time
     * </p>
     *
     * @param wrapper 条件对象.
     */
    @GetMapping("complex2")
    public ResponseEntity<IPage> complexQuery2(@QueryPredicate(QueryCondition.class) QueryPageWrapper<?> wrapper) {
        log.info(wrapper.getPage().getEw().getTargetSql());
        return ResponseEntity.ok(wrapper.getPage());
    }

    /**
     * 请求地址为: /complex3?userId=1&totalPrice=20000
     *
     * @param wrapper 包装请求对象
     * @return 分页结果
     */
    @GetMapping("complex3")
    public ResponseEntity<IPage<OrderInfo>> complexQuery3(@QueryPredicate(OrderQueryVo.class) QueryPageWrapper<OrderInfo> wrapper) {
        User user = new User();
        Order order = new Order();
        Goods goods = new Goods();
        JoinQueryWrapper queryWrapper = JoinWrappers.select(Order.class).join(User.class)
                .on(user::getId, order::getUserId)
                .join(Goods.class)
                .on(goods::getId, order::getGoodsId)
                .where(wrapper.getPage())
                .result(OrderInfo.class);
        IPage<OrderInfo> page = complexQuery.selectPage(queryWrapper);
        return ResponseEntity.ok(page);
    }
}
