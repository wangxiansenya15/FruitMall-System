package com.wxs.service;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.dto.UserDTO;
import com.wxs.pojo.dto.Role;
import com.wxs.pojo.entity.User;
import com.wxs.util.JwtUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    // 在类顶部添加如下注入代码：
    @Autowired
    private AuthenticationManager authenticationManager;

    public Result<UserDTO> authenticate(UserDTO userDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
            );
            log.info("用户凭证验证成功");

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails.getUsername());
            log.info("JWT令牌生成成功，token为: {}", token);

            // 获取用户详情（包含角色、头像等）
            User user = userService.getUserByUsername(userDetails.getUsername());
            if (user == null) {
                log.warn("用户不存在: {}", userDetails.getUsername());
                return Result.error(Result.BAD_REQUEST,"用户不存在");
            }

            Role role = user.getRole();
            if (role == null) {
                log.warn("用户详细信息缺失，用户名: {}", userDetails.getUsername());
                return Result.error(Result.BAD_REQUEST,"用户详细信息缺失");
            }

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            UserDTO responseUserDTO = new UserDTO(token, userDetails.getUsername(), roles);

            log.info("登录成功，返回200 OK响应");
            return Result.success("登录成功", responseUserDTO);

        } catch (BadCredentialsException ex) {
            log.warn("登录失败: 凭证无效 - {}", ex.getMessage());
            return Result.error(Result.BAD_REQUEST, "凭证无效,用户名或密码错误");
        } catch (Exception ex) {
            log.error("登录过程中发生异常: {}", ex.getClass().getName(), ex);
            return Result.error(Result.INTERNAL_ERROR,"登录过程中发生异常，请稍后再试");
        }
    }
}
