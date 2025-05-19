package com.wxs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtils.getTokenFromRequest(request);

        if (token != null && jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);
            UserDetails userDetails;

            try {
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                logger.warn("无法加载用户详情: {}");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未找到用户");
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            logger.info("JWT验证通过，用户：{}");
            // 设置认证信息到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
            // 只有在请求需要认证的路径时才记录警告
            if (!shouldNotFilter(request)) {
                logger.warn("无效或缺失Token，请求路径: {}");
            }
        }

        filterChain.doFilter(request, response);
    }


    /**
     * 跳过某些路径的过滤（如登录接口）
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return "/api/auth/login".equals(path) || "/api/auth/code".equals(path) || "/api/auth/register".equals(path);
    }
}

