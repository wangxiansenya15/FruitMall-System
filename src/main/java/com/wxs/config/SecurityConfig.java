package com.wxs.config;

import com.wxs.pojo.dto.Role;
import com.wxs.service.UserService;
import com.wxs.util.JwtAuthenticationFilter;
import com.wxs.util.JwtUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }

    /**
     * 安全过滤器链配置（6.x核心配置）
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // 公开访问路径
                        .requestMatchers("/static/**", "/auth/**","/avatar/**", "/products/**", "/product/**").permitAll()
                        // 角色权限控制
                        .requestMatchers("/goods/**",  "/user/**",  "/cart/**", "/orders/**","/payment/**").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER")
                        .requestMatchers("/**").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )
//                // 表单登录配置
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .loginProcessingUrl("/auth")
//                        .defaultSuccessUrl("/home")
//                        .failureUrl("/login?error=true")
//                )
                // 异常处理
                .exceptionHandling(e -> e
                        .accessDeniedPage("/403")
                )
//                // 登出配置
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout")
//                        .invalidateHttpSession(true)
//                )
                // 禁用CSRF（适用于REST API场景）
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 设置会话管理策略为无状态（适用于JWT认证）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 设置CORS配置;

        return http.build();
    }

    /**
     * 数据库实现的UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return username -> {
            // 通过UserService从数据库加载用户
            com.wxs.pojo.entity.User user = userService.getUserByUsername(username);
            // 如果用户不存在，抛出异常
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }

            // 检查 UserDetail 是否为 null
            Role role = user.getRole();
            if (role == null) {
                throw new BadCredentialsException("用户详细信息不存在");
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(new String[]{(user.getRole().name())})
                    .build();
        };
    }

    /**
     * BCrypt密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // 前端地址
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // 允许的请求方法
        config.setAllowedHeaders(List.of("*")); // 允许所有请求头
        config.setExposedHeaders(List.of("Authorization")); // 暴露给前端的头
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // 预检请求缓存时间

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return builder.build();
    }
}