package cn.itcast.service;

import cn.itcast.mapper.UserMapper;
import cn.itcast.pojo.User;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author:Administrator
 * @Date: 2019/11/24 9:11
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 1.根据用户名查询数据库中的user用户的详细信息
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
