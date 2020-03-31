package com.unbright.pagination.extension;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        Map<String, String> params = enumerationToStream(request.getParameterNames())
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

    private <T> Stream<T> enumerationToStream(Enumeration<T> en) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                new Iterator<T>() {
                    @Override
                    public T next() {
                        return en.nextElement();
                    }

                    @Override
                    public boolean hasNext() {
                        return en.hasMoreElements();
                    }
                },
                Spliterator.ORDERED), false);

    }
}
