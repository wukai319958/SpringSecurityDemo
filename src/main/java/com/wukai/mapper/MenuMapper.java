package com.wukai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wukai.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MenuMapper
 *
 * @author WuKai
 * @date 2022/5/4
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询用户所具备的权限列表
     * @param id
     * @return
     */
    List<String> selectPermsByUserId(Long id);
}
