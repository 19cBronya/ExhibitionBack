package com.example.exbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     * 定义密码编码器 Bean，使用 BCrypt 加密算法
     * @return BCryptPasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置安全过滤器链
     * @param http HttpSecurity 实例，用于配置安全规则
     * @return 配置好的 SecurityFilterChain
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置请求授权规则
        http.authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll() // 允许所有请求
                )
                // 禁用 CSRF 保护
                .csrf(csrf -> csrf.disable())
                // 配置会话管理策略为无状态会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * 配置内存中的用户认证信息
     * @param auth AuthenticationManagerBuilder 实例，用于构建认证管理器
     * @throws Exception 配置过程中可能抛出的异常
     */
    @Bean
    public AuthenticationManagerBuilder configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}123456") // 使用 {noop} 前缀表示明文密码
                .roles("ORGANIZER");
        return auth;
    }
}