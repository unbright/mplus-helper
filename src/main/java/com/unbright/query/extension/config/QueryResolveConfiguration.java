package com.unbright.query.extension.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created with IDEA
 * ProjectName: mplus-helper
 * Date: 2020/4/16
 * Time: 10:04
 *
 * @author WZP
 * @version v1.0
 */
@Configuration
public class QueryResolveConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new QueryArgumentResolver());
    }
}
