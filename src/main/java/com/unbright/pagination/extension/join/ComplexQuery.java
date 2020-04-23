package com.unbright.pagination.extension.join;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.unbright.pagination.extension.join.query.JoinQueryWrapper;
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
        String msId = injectMappedStatement(wrapper);
        return sqlSession.selectList(msId, getParams(wrapper));
    }

    public <T> T selectOne(JoinQueryWrapper wrapper) {
        String msId = injectMappedStatement(wrapper);
        return sqlSession.selectOne(msId, getParams(wrapper));
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

    private String injectMappedStatement(JoinQueryWrapper wrapper) {
        SqlMethod sqlMethod = SqlMethod.SELECT_LIST;
        String sql = String.format(sqlMethod.getSql(), wrapper.getSqlFirst(),
                wrapper.getSqlSelect(),
                wrapper.getFromTable(),
                wrapper.getSqlSegment(),
                wrapper.getSqlComment());
        String msId = createMsId(wrapper.getResultClass() + sql, SqlCommandType.SELECT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource source = languageDriver.createSqlSource(configuration, sql, wrapper.getResultClass());
        newSelectMappedStatement(msId, source, wrapper.getResultClass());
        return msId;
    }

    private String createMsId(String sql, SqlCommandType sqlCommandType) {
        return sqlCommandType.toString() + "." + sql.hashCode();
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
