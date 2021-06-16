package com.unbright.query.extension.join;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.unbright.query.extension.join.query.JoinQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/22
 * Time: 13:53
 *
 * @author WZP
 * @version v1.0
 */
@Slf4j
@Component
public class ComplexQuery {

    private final SqlSession sqlSession;

    private final Configuration configuration;
    private final LanguageDriver languageDriver;

    public ComplexQuery(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.configuration = sqlSession.getConfiguration();
        this.languageDriver = this.configuration.getDefaultScriptingLanguageInstance();
    }

    public <T> List<T> selectList(JoinQueryWrapper wrapper) {
        String msId = createMappedStatement(createSelectSql(wrapper), wrapper.getResultClass());
        return sqlSession.selectList(msId, getParams(wrapper));
    }

    public <T> T selectOne(JoinQueryWrapper wrapper) {
        String msId = createMappedStatement(createSelectSql(wrapper), wrapper.getResultClass());
        return sqlSession.selectOne(msId, getParams(wrapper));
    }

    public <T> IPage<T> selectPage(JoinQueryWrapper wrapper) {
        String countMsId = createMappedStatement(createCountSql(wrapper), long.class);
        long total = sqlSession.selectOne(countMsId, getParams(wrapper));
        List<T> records = this.selectLimit(wrapper);
        IPage<T> page = wrapper.getPage();
        page.setTotal(total);
        page.setRecords(records);
        return page;
    }

    public <T> List<T> selectLimit(JoinQueryWrapper wrapper) {
        wrapper.last(wrapper.getLimitSql());
        return this.selectList(wrapper);
    }

    /**
     * 创建一个查询的MS
     *
     * @param msId       MappedStatement id
     * @param sqlSource  执行的sqlSource
     * @param resultType 返回的结果类型
     */
    private void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
        MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT)
                .resultMaps(new ArrayList<ResultMap>() {
                    {
                        add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<>(0)).build());
                    }
                })
                .build();
        //缓存
        configuration.addMappedStatement(ms);
    }

    private String createMappedStatement(String sql, Class<?> resultType) {
        String msId = createMsId(resultType + sql);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource source = languageDriver.createSqlSource(configuration, sql, Object.class);
        newSelectMappedStatement(msId, source, resultType);
        return msId;
    }

    /**
     * 生成统计查询sql.
     *
     * @param wrapper wrapper
     * @return sql
     */
    private String createCountSql(JoinQueryWrapper wrapper) {
        SqlMethod sqlMethod = SqlMethod.SELECT_COUNT;
        String sql = String.format(sqlMethod.getSql(), wrapper.getSqlFirst(),
                "1", wrapper.getFromTable(), wrapper.getSqlSegment(), wrapper.getSqlComment());
        log.info("CREATE COUNT SQL ====> {}", sql);
        return sql;
    }

    /**
     * 生成普通查询sql.
     *
     * @param wrapper wrapper
     * @return sql
     */
    private String createSelectSql(JoinQueryWrapper wrapper) {
        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
        String sql = String.format(sqlMethod.getSql(), wrapper.getSqlFirst(),
                wrapper.getSqlSelect(), wrapper.getFromTable(),
                wrapper.getSqlSegment(), "", wrapper.getSqlComment());
        log.info("CREATE SELECT SQL ====> {}", sql);
        return sql;
    }

    private String createMsId(String sql) {
        return SqlCommandType.SELECT.toString() + "." + sql.hashCode();
    }

    private Map<String, JoinQueryWrapper> getParams(JoinQueryWrapper wrapper) {
        if (MapUtils.isEmpty(wrapper.getParamNameValuePairs())) {
            return null;
        }
        Map<String, JoinQueryWrapper> param = new HashMap<>(1);
        param.put(Constants.WRAPPER, wrapper);
        return param;
    }

    private boolean hasMappedStatement(String msId) {
        return configuration.hasStatement(msId, false);
    }
}
