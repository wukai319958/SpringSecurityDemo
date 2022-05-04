package com.wukai.service;

import com.wukai.domain.dto.UserLoginDto;

/**
 * LoginService
 *
 * @author WuKai
 * @date 2022/5/3
 */
public interface LoginService {

    String login(UserLoginDto user);

    boolean logout();
}
