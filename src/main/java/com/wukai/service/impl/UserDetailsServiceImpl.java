package com.wukai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wukai.domain.User;
import com.wukai.domain.security.LoginUser;
import com.wukai.mapper.MenuMapper;
import com.wukai.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * UserDetailsServiceImpl
 *
 * @author WuKai
 * @date 2022/5/3
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            log.error("无法找到用户名为:" + username + "的用户");
            throw new RuntimeException("无法找到用户名为:" + username + "的用户");
        }

        // 从数据库中查询对应的权限信息存入UserDetails中
        List<String> authorities = menuMapper.selectPermsByUserId(user.getId());
        // 将数据封装成UserDetails返回
        return new LoginUser(user, authorities);
    }
}
