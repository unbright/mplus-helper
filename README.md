# mplus-helper
MybatisPlus自定义分页对象,实现自定义筛选条件解析

## 使用方法
可参考代码中的DemoController

##### 简单查询条件
```java
@GetMapping
public ResponseEntity<Page<Haphazard>> query1(@RequestParam String categoryId) {
    //根据请求参数封装对象,会自动根据page和size分页,若有其他条件会自动拼装where
    // ?id=123 => where id = 123
    QueryPage<Haphazard> page = smartInitPage();
    //拼接自己的查询条件
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
```

##### 复杂查询条件，自定义sql
例如自定义sql为如下
```sql
select * from t1 join t2 on t1.id = t2.a_id
where t1.a_name like ? and t2.cate_id = ?
and (t1.key like ? or t2.key like ?)
order by a.time desc
```
```java
//只需传递参数 t1.a_name|LIKE=参数&t2.cate_id=参数
@GetMapping
public ResponseEntity<IPage> joinQuery(@RequestParam String keywords) {
    //smartInitPage会自动封装如上所需条件
    //如果想手动添加条件如 t1.key like ? or t2.key like ? keywords,只需使用orLikeMulti
    QueryPage<?> page = smartInitPage("keywords");
    page.orLikeMulti(keywords, "a.key", "b.key");
    //排序
    page.addOrderByDesc("a.time");
    return ResponseEntity.ok(demoService.customQuery(page));
}
```

