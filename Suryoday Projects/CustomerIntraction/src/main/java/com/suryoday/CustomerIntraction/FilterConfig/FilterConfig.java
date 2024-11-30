package com.suryoday.CustomerIntraction.FilterConfig;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.suryoday.CustomerIntraction.Filter.CustomFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomFilter> customFilter() {
        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomFilter());
        registrationBean.addUrlPatterns("/CustomerIntraction/*");
        return registrationBean;
    }
}
