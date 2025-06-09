package com.wxs.exception;

import com.wxs.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 检查当前请求是否为 Swagger 相关路径
     * 对于 Swagger 文档生成请求，不应该被全局异常处理器拦截
     */
    private boolean isSwaggerRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String requestURI = request.getRequestURI();
            return requestURI.startsWith("/v3/api-docs") || 
                   requestURI.startsWith("/swagger-ui") || 
                   requestURI.startsWith("/swagger-resources") ||
                   requestURI.contains("api-docs");
        }
        return false;
    }


    // 资源不存在异常 (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    Result<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return Result.notFound(ex.getMessage());
    }

    // 参数校验异常 (400)
    @ExceptionHandler(ValidationException.class)
    public Result<?> handleValidationException(ValidationException ex) {
        return Result.badRequest(ex.getMessage());
    }

    // 认证异常 (401)
    @ExceptionHandler(AuthenticationException.class)
    public Result<?> handleAuthenticationException(AuthenticationException ex) {
        return Result.unauthorized(ex.getMessage());
    }

    // 权限不足异常 (403)
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException ex) {
        return Result.forbidden(ex.getMessage());
    }

    // 全局异常兜底处理 (500)
    @ExceptionHandler(Exception.class)
    public Result<?> handleGlobalException(Exception ex) {
        // 如果是 Swagger 相关请求，不拦截异常，让其正常抛出
        if (isSwaggerRequest()) {
            throw new RuntimeException(ex);
        }
        
        log.error("系统异常: ", ex);
        return Result.serverError("系统繁忙，请稍后重试");
    }
}