package cn.itcast.service;

import cn.itcast.pojo.User;

/**
 * @Author:Administrator
 * @Date: 2019/11/23 21:42
 */
public interface UserService {
    User findByUsername(String username);
}
