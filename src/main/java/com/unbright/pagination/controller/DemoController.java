package com.unbright.pagination.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unbright.pagination.entity.Haphazard;
import com.unbright.pagination.extension.BaseController;
import com.unbright.pagination.extension.QueryPage;
import com.unbright.pagination.service.DemoService;
import com.unbright.pagination.vo.QueryCondition;
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

    private final DemoService demoService;

    /**
     * 单表查询
     */
    @GetMapping("simple")
    public ResponseEntity<Page<Haphazard>> simpleQuery(@RequestParam String categoryId) {
        // ?id=123 => where id = 123
        QueryPage<Haphazard> page = smartInitPage();
        //and category_id = ?
        page.addEq(Haphazard::getCategoryId, categoryId);
        //and name like '%name%'
        page.addLike(Haphazard::getName, "name");
        //and (name like '%name%' or description like '%name%')
        //page.orLikeMulti("name", Haphazard::getName, Haphazard::getDescription);
        //order by create_time desc
        page.addOrderByDesc(Haphazard::getCreateTime);
        return ResponseEntity.ok(demoService.page(page, page.getEw()));
    }

    /**
     * 多表join查询.
     * select * from a join b on a.id = b.a_id ${ew.customSqlSegment}
     * ?a.a_name|LIKE=11&b.a_id=12&a.time|DESC=a.time,b.time
     * => where a.a_name like ? and b.a_id = ? order by a.time,b.time desc
     */
    @GetMapping("join")
    public ResponseEntity<IPage> joinQuery(@RequestParam String keywords) {
        QueryPage<?> page = smartInitPage("keywords");
        //add custom condition
        //and (a.key like ? or b.key like ?)
        page.orLikeMulti(keywords, "a.key", "b.key");
        return ResponseEntity.ok(demoService.customQuery(smartInitPage()));
    }

    /**
     * 自定义注解复杂查询.
     *
     * @param condition 条件对象.
     */
    @GetMapping("complex")
    public ResponseEntity<IPage> complexQuery(QueryCondition condition) {
        QueryPage<?> page = smartQuery(condition);
        log.info(page.getEw().getTargetSql());
        return ResponseEntity.ok(page);
    }
}
