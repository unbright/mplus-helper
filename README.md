# mplus-helper
MybatisPlus自定义分页对象,实现自定义筛选条件解析

## 使用方法
可参考代码中的DemoController

### 简单查询条件
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

### 复杂查询条件，自定义sql
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

### 使用注解声明查询条件
如下例子的SQL语句为 where  (t1.nickname LIKE ? OR t2.nickname LIKE ?) AND (age >= ? OR t1.type = ?) ORDER BY create_time DESC
```java
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
```

**使用@QueryPredicate参数注解自动注入分页对象**
```java
    /**
     * QueryPredicate中如果不传类型，则使用request param为查询条件.
     */
    @GetMapping("complex2")
    public ResponseEntity<IPage> complexQuery2(@QueryPredicate(QueryCondition.class) QueryPageWrapper<?> wrapper) {
        log.info(wrapper.getPage().getEw().getCustomSqlSegment());
        return ResponseEntity.ok(wrapper.getPage());
    }
```
### 使用查询构造器构造复杂查询

#### 1.创建查询返回结果对象
示例:
```java
@Data
public class OrderInfo {

    @Alias(entity = Order.class)
    private String id;

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
```
***实例中使用Alias注解声明查询的别名,name表示查询字段名称，entity表示所属对象,属性名字表示查询结果别名***

#### 2.构造查询方法
示例:
```java
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
        List<OrderInfo> dtos = complexQuery.selectList(wrapper);
        System.out.println(dtos);
    }
}
```
输出sql如下,本质使用的是Mybatis Plus的sql构造器，所以不用担心sql注入
```sql
SELECT
	od.id AS id,
	od.total_price AS totalPrice,
	od.create_time AS createTime,
	od.number AS number,
	u.NAME AS username,
	g.NAME AS goodsName,
	g.stock AS stock 
FROM
	t_order AS od
	JOIN t_user AS u ON u.id = od.user_id
	JOIN t_goods AS g ON g.id = od.goods_id 
WHERE
	(
	u.id = #{ew.paramNameValuePairs.MPGENVAL1} AND od.total_price >= #{ew.paramNameValuePairs.MPGENVAL2}) ORDER BY od.create_time DESC
```

### 使用条件构造器和自定义条件组合查询
```java
@GetMapping("complex3")
    public ResponseEntity<IPage<OrderInfo>> complexQuery3(@QueryPredicate(OrderQueryVo.class) QueryPageWrapper<OrderInfo> wrapper) {
        User user = new User();
        Order order = new Order();
        Goods goods = new Goods();
        //使用where方法可传入QueryPage对象，自动构造查询条件
        JoinQueryWrapper queryWrapper = JoinWrappers.select(Order.class).as("od").join(User.class).as("u")
                .on(user::getId, order::getUserId)
                .join(Goods.class).as("g")
                .on(goods::getId, order::getGoodsId)
                .where(wrapper.getPage())
                .result(OrderInfo.class);
        IPage<OrderInfo> page = complexQuery.selectPage(queryWrapper);
        return ResponseEntity.ok(page);
    }
```
