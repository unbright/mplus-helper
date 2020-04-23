package com.unbright.pagination.extension.join.support;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/22
 * Time: 14:24
 *
 * @author WZP
 * @version v1.0
 */
@Component
public class ComplexQuery {
    private final MybatisQueryHelper queryHelper;
    private final SqlSession sqlSession;

    /**
     * 构造方法，默认缓存MappedStatement
     *
     * @param sqlSession
     */
    public ComplexQuery(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.queryHelper = new MybatisQueryHelper(sqlSession.getConfiguration());
    }

    /**
     * 获取List中最多只有一个的数据
     *
     * @param list List结果
     * @param <T>  泛型类型
     * @return
     */
    private <T> T getOne(List<T> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        } else {
            return null;
        }
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql 执行的sql
     * @return
     */
    public Map<String, Object> selectOne(String sql) {
        List<Map<String, Object>> list = selectList(sql);
        return getOne(list);
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
    public Map<String, Object> selectOne(String sql, Object value) {
        List<Map<String, Object>> list = selectList(sql, value);
        return getOne(list);
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql        执行的sql
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
    public <T> T selectOne(String sql, Class<T> resultType) {
        List<T> list = selectList(sql, resultType);
        return getOne(list);
    }

    /**
     * 查询返回一个结果，多个结果时抛出异常
     *
     * @param sql        执行的sql
     * @param value      参数
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
    public <T> T selectOne(String sql, Object value, Class<T> resultType) {
        List<T> list = selectList(sql, value, resultType);
        return getOne(list);
    }

    /**
     * 查询返回List<Map<String, Object>>
     *
     * @param sql 执行的sql
     * @return
     */
    public List<Map<String, Object>> selectList(String sql) {
        String msId = queryHelper.select(sql);
        return sqlSession.selectList(msId);
    }

    /**
     * 查询返回List<Map<String, Object>>
     *
     * @param sql   执行的sql
     * @param value 参数
     * @return
     */
    public List<Map<String, Object>> selectList(String sql, Object value) {
        Class<?> parameterType = value != null ? value.getClass() : null;
        String msId = queryHelper.selectDynamic(sql, parameterType);
        return sqlSession.selectList(msId, value);
    }

    /**
     * 查询返回指定的结果类型
     *
     * @param sql        执行的sql
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
    public <T> List<T> selectList(String sql, Class<T> resultType) {
        String msId;
        if (resultType == null) {
            msId = queryHelper.select(sql);
        } else {
            msId = queryHelper.select(sql, resultType);
        }
        return sqlSession.selectList(msId);
    }

    /**
     * 查询返回指定的结果类型
     *
     * @param sql        执行的sql
     * @param value      参数
     * @param resultType 返回的结果类型
     * @param <T>        泛型类型
     * @return
     */
    public <T> List<T> selectList(String sql, Object value, Class<T> resultType) {
        String msId;
        Class<?> parameterType = value != null ? value.getClass() : null;
        if (resultType == null) {
            msId = queryHelper.selectDynamic(sql, parameterType);
        } else {
            msId = queryHelper.selectDynamic(sql, parameterType, resultType);
        }
        return sqlSession.selectList(msId, value);
    }
}
