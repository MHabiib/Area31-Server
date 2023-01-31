package com.skripsi.area31.config;

import com.skripsi.area31.util.FieldName;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration @EnableResourceServer public class ResourceServerConfig
    extends ResourceServerConfigurerAdapter {

    @Override public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(FieldName.Constants.RESOURCE_ID).stateless(false);
    }

    @Override public void configure(HttpSecurity http) throws Exception {
        http.
            anonymous().disable().authorizeRequests().antMatchers("/api/student/**")
            .access("hasRole('ROLE_STUDENT')").and().
            anonymous().disable().authorizeRequests().antMatchers("/api/admin/**")
            .access("hasRole('ROLE_ADMIN')").and().
            anonymous().disable().authorizeRequests().antMatchers("/api/instructor/**")
            .access("hasRole('ROLE_INSTRUCTOR')").and().exceptionHandling()
            .accessDeniedHandler(new OAuth2AccessDeniedHandler()).and().authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll();
    }

}
