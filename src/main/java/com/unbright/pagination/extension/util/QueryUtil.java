package com.unbright.pagination.extension.util;

import com.unbright.pagination.extension.QueryPage;
import com.unbright.pagination.extension.annotation.QueryStatement;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.collections4.MapUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/16
 * Time: 9:38
 *
 * @author WZP
 * @version v1.0
 */
public final class QueryUtil {

    /**
     * 根据请求参数构建page.
     *
     * @return page查询
     */
    public static <T> QueryPage<T> initPage(HttpServletRequest request, String... ignoreProperties) {
        Map<String, String> params = EnumerationUtils.toList(request.getParameterNames())
                .stream()
                .collect(Collectors.toMap(k -> k, request::getParameter));
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        params.remove("page");
        params.remove("size");
        Arrays.stream(ignoreProperties).forEach(params::remove);
        return new QueryPage<>(page, size, params);
    }

    /**
     * 根据请求参数构建page.
     *
     * @return page查询
     */
    public static <T> QueryPage<T> smartInitPage(HttpServletRequest request, String... ignoreProperties) {
        QueryPage<T> queryPage = initPage(request, ignoreProperties);
        queryPage.clear();
        Map<String, String> params = queryPage.getParams();
        return queryPage.resolve(params);
    }

    /**
     * 初始化查询.
     *
     * @param obj 条件对象.
     * @param <T> T
     * @return page
     */
    public static <T> QueryPage<T> smartQuery(HttpServletRequest request, Object obj) {
        QueryPage<T> page = smartInitPage(request, "*");
        page.clear();
        QueryStatement<T> statement = new QueryStatement<>(page);
        return statement.createQuery(obj);
    }
}
