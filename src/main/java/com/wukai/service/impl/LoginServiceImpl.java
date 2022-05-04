package com.wukai.service.impl;

import com.wukai.domain.dto.UserLoginDto;
import com.wukai.domain.security.LoginUser;
import com.wukai.service.LoginService;
import com.wukai.utils.JwtUtil;
import com.wukai.utils.RedisCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * LoginServiceImpl
 *
 * @author WuKai
 * @date 2022/5/3
 */
@Service
@Log4j2
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public String login(UserLoginDto user) {

        // 1.使用AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        // 最后实际调用 UserDetailsService的loadUserByUsername方法进行用户校验
        Authentication authenticateResult = authenticationManager.authenticate(authenticationToken);
        // 2.如果认证通过，使用userid生成一个jwt并返回，如果认证失败，返回null
        if (Objects.isNull(authenticateResult)) {
            log.warn("用户" + user.getUserName() + "认证失败！");
            throw new RuntimeException("登录失败！");
        } else {
            LoginUser loginUser = (LoginUser) authenticateResult.getPrincipal();
            String userId = loginUser.getUser().getId().toString();
            String jwt = JwtUtil.createJWT(userId);
            // 3.将完userid作为key，完整的用户信息作为value存入redis
            redisCache.setCacheObject("login:" + userId, loginUser);
            log.info("用户:" + user.getUserName() + "登录成功！");
            return jwt;
        }
    }

    @Override
    public boolean logout() {
        LoginUser loginUser = null;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            loginUser = (LoginUser) authentication.getPrincipal();
            Long userId = loginUser.getUser().getId();
            redisCache.deleteObject("login:" + userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户名为" + loginUser.getUser().getUserName() + "的用户注销失败！");
            return false;
        }
    }
}
