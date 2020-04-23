package com.unbright.pagination.extension.join.support;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/22
 * Time: 14:43
 *
 * @author WZP
 * @version v1.0
 */
class MybatisQueryHelper {

    private Configuration configuration;
    private LanguageDriver languageDriver;

    MybatisQueryHelper(Configuration configuration) {
        this.configuration = configuration;
        languageDriver = configuration.getDefaultScriptingLanguageInstance();
    }

    /**
     * 创建MSID
     *
     * @param sql 执行的sql
     * @param sql 执行的sqlCommandType
     * @return
     */
    String newMsId(String sql, SqlCommandType sqlCommandType) {
        StringBuilder msIdBuilder = new StringBuilder(sqlCommandType.toString());
        msIdBuilder.append(".").append(sql.hashCode());
        return msIdBuilder.toString();
    }

    /**
     * 是否已经存在该ID
     *
     * @param msId
     * @return
     */
    boolean hasMappedStatement(String msId) {
        return configuration.hasStatement(msId, false);
    }

    /**
     * 创建一个查询的MS
     *
     * @param msId
     * @param sqlSource  执行的sqlSource
     * @param resultType 返回的结果类型
     */
    void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType) {
        MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, SqlCommandType.SELECT)
                .resultMaps(new ArrayList<ResultMap>() {
                    {
                        add(new ResultMap.Builder(configuration, "defaultResultMap", resultType, new ArrayList<ResultMapping>(0)).build());
                    }
                })
                .build();
        //缓存
        configuration.addMappedStatement(ms);
    }

    /**
     * 创建一个简单的MS
     *
     * @param msId
     * @param sqlSource      执行的sqlSource
     * @param sqlCommandType 执行的sqlCommandType
     */
    void newUpdateMappedStatement(String msId, SqlSource sqlSource, SqlCommandType sqlCommandType) {
        MappedStatement ms = new MappedStatement.Builder(configuration, msId, sqlSource, sqlCommandType)
                .resultMaps(new ArrayList<ResultMap>() {
                    {
                        add(new ResultMap.Builder(configuration, "defaultResultMap", int.class, new ArrayList<ResultMapping>(0)).build());
                    }
                })
                .build();
        //缓存
        configuration.addMappedStatement(ms);
    }

    String select(String sql) {
        String msId = newMsId(sql, SqlCommandType.SELECT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
        newSelectMappedStatement(msId, sqlSource, Map.class);
        return msId;
    }

    String selectDynamic(String sql, Class<?> parameterType) {
        String msId = newMsId(sql + parameterType, SqlCommandType.SELECT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
        newSelectMappedStatement(msId, sqlSource, Map.class);
        return msId;
    }

    String select(String sql, Class<?> resultType) {
        String msId = newMsId(resultType + sql, SqlCommandType.SELECT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
        newSelectMappedStatement(msId, sqlSource, resultType);
        return msId;
    }

    String selectDynamic(String sql, Class<?> parameterType, Class<?> resultType) {
        String msId = newMsId(resultType + sql + parameterType, SqlCommandType.SELECT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
        newSelectMappedStatement(msId, sqlSource, resultType);
        return msId;
    }

    String insert(String sql) {
        String msId = newMsId(sql, SqlCommandType.INSERT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
        newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
        return msId;
    }

    String insertDynamic(String sql, Class<?> parameterType) {
        String msId = newMsId(sql + parameterType, SqlCommandType.INSERT);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
        newUpdateMappedStatement(msId, sqlSource, SqlCommandType.INSERT);
        return msId;
    }

    String update(String sql) {
        String msId = newMsId(sql, SqlCommandType.UPDATE);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
        newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
        return msId;
    }

    String updateDynamic(String sql, Class<?> parameterType) {
        String msId = newMsId(sql + parameterType, SqlCommandType.UPDATE);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
        newUpdateMappedStatement(msId, sqlSource, SqlCommandType.UPDATE);
        return msId;
    }

    String delete(String sql) {
        String msId = newMsId(sql, SqlCommandType.DELETE);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        StaticSqlSource sqlSource = new StaticSqlSource(configuration, sql);
        newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
        return msId;
    }

    String deleteDynamic(String sql, Class<?> parameterType) {
        String msId = newMsId(sql + parameterType, SqlCommandType.DELETE);
        if (hasMappedStatement(msId)) {
            return msId;
        }
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, parameterType);
        newUpdateMappedStatement(msId, sqlSource, SqlCommandType.DELETE);
        return msId;
    }
}
