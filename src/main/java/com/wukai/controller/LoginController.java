package com.wukai.controller;

import com.wukai.common.ResponseResult;
import com.wukai.domain.dto.UserLoginDto;
import com.wukai.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * LoginController
 *
 * @author WuKai
 * @date 2022/5/3
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody UserLoginDto user) {
        String jwt = loginService.login(user);
        Map<String, String> userMap = new HashMap<>(1);
        userMap.put("token", jwt);
        return new ResponseResult(200, "登录成功", userMap);
    }


    /**
     * 注销
     *
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout() {
        boolean result = loginService.logout();
        if (result) {
            return new ResponseResult(200, "用户注销成功！");
        } else {
            return new ResponseResult(400, "用户注销失败！");
        }
    }

}
