package org.grails.plugins.web.controllers;

import grails.config.Settings;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.grails.spring.config.http.GrailsFilters;
import org.grails.web.filters.HiddenHttpMethodFilter;
import org.grails.web.servlet.mvc.GrailsWebRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.EnumSet;

@AutoConfiguration(before = { HttpEncodingAutoConfiguration.class, WebMvcAutoConfiguration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ControllersAutoConfiguration {

    @Value("${" + Settings.FILTER_ENCODING + ":utf-8}")
    private String filtersEncoding;

    @Value("${" + Settings.FILTER_FORCE_ENCODING + ":false}")
    private boolean filtersForceEncoding;

    @Bean
    @ConditionalOnMissingBean(CharacterEncodingFilter.class)
    public CharacterEncodingFilter characterEncodingFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        OrderedCharacterEncodingFilter characterEncodingFilter = new OrderedCharacterEncodingFilter();
        characterEncodingFilter.setEncoding(filtersEncoding);
        characterEncodingFilter.setForceEncoding(filtersForceEncoding);
        characterEncodingFilter.setOrder(GrailsFilters.CHARACTER_ENCODING_FILTER.getOrder());
        return characterEncodingFilter;
    }

    @Bean
    @ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
    public FilterRegistrationBean<Filter> hiddenHttpMethodFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HiddenHttpMethodFilter());
        registrationBean.addUrlPatterns(Settings.DEFAULT_WEB_SERVLET_PATH);
        registrationBean.setOrder(GrailsFilters.HIDDEN_HTTP_METHOD_FILTER.getOrder());
        return registrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(GrailsWebRequestFilter.class)
    public FilterRegistrationBean<Filter> grailsWebRequestFilter(ApplicationContext applicationContext) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        GrailsWebRequestFilter grailsWebRequestFilter = new GrailsWebRequestFilter();
        grailsWebRequestFilter.setApplicationContext(applicationContext);
        registrationBean.setFilter(grailsWebRequestFilter);
        registrationBean.setDispatcherTypes(EnumSet.of(
                DispatcherType.FORWARD,
                DispatcherType.INCLUDE,
                DispatcherType.REQUEST)
        );
        registrationBean.addUrlPatterns(Settings.DEFAULT_WEB_SERVLET_PATH);
        registrationBean.setOrder(GrailsFilters.GRAILS_WEB_REQUEST_FILTER.getOrder());
        return registrationBean;
    }
}
