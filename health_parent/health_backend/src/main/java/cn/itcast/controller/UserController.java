package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:Administrator
 * @Date: 2019/11/24 11:50
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 1.获取当前登录的用户的用户名
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
