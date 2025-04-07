// filepath: /Users/ccmora/projects/01_spring/userscrud/src/main/java/com/morataya/userscrud/config/FilterConfig.java
package com.morataya.userscrud.config;

import com.morataya.userscrud.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import javax.servlet.Filter;
import jakarta.servlet.Filter;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/api/*"); // Aplica el filtro a todos los endpoints bajo /api/
        registrationBean.setOrder(1); // Orden de ejecución del filtro
        return registrationBean;
    }
}