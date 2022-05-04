package com.wukai.domain.dto;

import lombok.Data;

/**
 * UserLoginDto
 *
 * @author WuKai
 * @date 2022/5/3
 */
@Data
public class UserLoginDto {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
