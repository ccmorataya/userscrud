package com.morataya.userscrud.config;

import com.morataya.userscrud.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.servlet.Filter;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}