package com.unbright.pagination.extension.config;

import com.unbright.pagination.extension.QueryPage;
import com.unbright.pagination.extension.annotation.QueryPredicate;
import com.unbright.pagination.extension.util.QueryUtil;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/16
 * Time: 9:44
 *
 * @author WZP
 * @version v1.0
 */
public class QueryArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(QueryPage.class)
                && parameter.hasParameterAnnotation(QueryPredicate.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        QueryPredicate predicate = parameter.getParameterAnnotation(QueryPredicate.class);
        Class<?> clazz = predicate.value();
        if (clazz != void.class) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            String name = clazz.getSimpleName();
            WebDataBinder binder = binderFactory.createBinder(webRequest, instance, name);
            if (binder.getTarget() != null) {
                if (!modelAndViewContainer.isBindingDisabled(name)) {
                    MutablePropertyValues values = new MutablePropertyValues(webRequest.getParameterMap());
                    binder.bind(values);
                }
            }
            return QueryUtil.smartQuery((HttpServletRequest) webRequest.getNativeRequest(), instance);
        } else {
            return QueryUtil.smartInitPage((HttpServletRequest) webRequest.getNativeRequest(), "*");
        }
    }
}
