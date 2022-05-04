package com.wukai.filter;

import com.wukai.domain.security.LoginUser;
import com.wukai.utils.JwtUtil;
import com.wukai.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * JwtAuthenticationTokenFilter
 *
 * @author WuKai
 * @date 2022/5/3
 */
@Component
@Log4j2
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    // 继承OncePerRequestFilter接口，保证过滤器只被调用一次，默认Filter在不同的Servlet版本中会有问题

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1.获取token
        String token = request.getHeader("token");
        // 请求头中没有token，直接放行，让后续验证过滤器进行验证
        if (StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2.解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("token非法");
            throw new RuntimeException("token非法");
        }
        // 3.从redis中获取用户信息
        String redisKey = "login:" + userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            log.error("用户未登录");
            throw new RuntimeException("用户未登录");
        }
        // 4.存入SecurityContextHolder（过滤器调用链中依赖这个组件获取用户信息）
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
