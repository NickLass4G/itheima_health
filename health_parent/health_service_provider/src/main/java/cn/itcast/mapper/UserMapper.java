package cn.itcast.mapper;

import cn.itcast.pojo.User;

/**
 * @Author:Administrator
 * @Date: 2019/11/24 9:14
 */
public interface UserMapper {
    User findByUsername(String username);
}
