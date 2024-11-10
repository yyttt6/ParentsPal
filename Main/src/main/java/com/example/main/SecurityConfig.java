package com.example.main;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // 为了测试的时候跳过security部分，本质上是禁用security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()  // 允许所有请求
                )
                .securityMatcher("/login", "/logout")  // 如果需要特定路径的安全匹配
                .csrf(AbstractHttpConfigurer::disable);  // 禁用 CSRF 保护

        return http.build();
    }
}
