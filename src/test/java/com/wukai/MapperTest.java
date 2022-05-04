package com.wukai;

import com.wukai.domain.User;
import com.wukai.mapper.MenuMapper;
import com.wukai.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * MapperTest
 *
 * @author WuKai
 * @date 2022/5/3
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void testBCryptPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 原文一样，但是每次加密时都会生成随机的盐，所以加密后的密文都不一样
        /*
        String encode = bCryptPasswordEncoder.encode("1234");
        String encode2 = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);
        System.out.println(encode2);
        */

        // 原文和密文可以匹配
        Assertions.assertTrue(bCryptPasswordEncoder.matches("1234", "$2a$10$7xmAZtP/eXfS.7IIYfoOAesIn5yMXbnwGbZ5k2V1Ej8ulxX8fzIk6"));


    }


    @Test
    public void testUserMapper() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }


    @Test
    public void testSelectPermsByUserId() {
        List<String> list = menuMapper.selectPermsByUserId(2L);
        System.out.println(list);
        Assertions.assertNotNull(list);
    }


}
