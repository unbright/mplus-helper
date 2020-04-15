package com.unbright.pagination.extension;

import com.unbright.pagination.extension.annotation.QueryStatement;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/3/31
 * Time: 15:19
 *
 * @author WZP
 * @version v1.0
 */
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    /**
     * 根据请求参数构建page.
     *
     * @return page查询
     */
    protected <T> QueryPage<T> initPage(String... ignoreProperties) {
        Map<String, String> params = EnumerationUtils.toList(request.getParameterNames())
                .stream()
                .collect(Collectors.toMap(k -> k, v -> request.getParameter(v)));
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
    protected <T> QueryPage<T> smartInitPage(String... ignoreProperties) {
        QueryPage<T> queryPage = initPage(ignoreProperties);
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
    protected <T> QueryPage<T> smartQuery(Object obj) {
        QueryStatement<T> statement = new QueryStatement<>(smartInitPage("*"));
        return statement.createQuery(obj);
    }
}
