/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hdygxsj.dida.api.configuration;

import com.hdygxsj.dida.api.authentication.EntryPointUnauthorizedHandler;
import com.hdygxsj.dida.api.authentication.RequestAccessDeniedHandler;
import com.hdygxsj.dida.api.authentication.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SpringSecurityConfiguration {

    public static final String PATH_PATTERN = "/**";

    public static final String LOGIN_INTERCEPTOR_PATH_PATTERN = "/**/*";

    public static final String LOGIN_PATH_PATTERN = "/**/login";
    public static final String REGISTER_PATH_PATTERN = "/users/register";

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private RequestAccessDeniedHandler requestAccessDeniedHandler;

    @Autowired
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;


    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity,

                                         JwtAuthenticationSecurityConfig jwtAuthenticationSecurityConfig
    ) throws Exception {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration(PATH_PATTERN, config);
        corsConfigurationSource.registerCorsConfiguration(PATH_PATTERN, config);
        httpSecurity
                .authorizeRequests()
                .antMatchers(LOGIN_PATH_PATTERN, REGISTER_PATH_PATTERN,
                        "/swagger-resources/**", "/webjars/**", "/v3/api-docs/**",
                        "/api-docs/**", "/swagger-ui.html",
                        "/doc.html", "/swagger-ui/**", "*.html", "/ui/**", "/error")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .cors()
                .configurationSource(corsConfigurationSource)
                .and()
                .apply(jwtAuthenticationSecurityConfig)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPointUnauthorizedHandler)
                .accessDeniedHandler(requestAccessDeniedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        ;

        return httpSecurity.build();
    }


}
