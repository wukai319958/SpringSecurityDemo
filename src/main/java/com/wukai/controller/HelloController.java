package com.wukai.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController
 *
 * @author WuKai
 * @date 2022/5/3
 */
@RestController
public class HelloController {

    @GetMapping("hello")
    // @PreAuthorize("hasAuthority('system:dept:list')")
    // 使用SPEL表达式执行自己的权限器
    @PreAuthorize("ex.hasAuthority('system:dept:list')")
    public String hello() {
        return "hello";
    }
}