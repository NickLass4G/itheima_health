package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/23 11:38
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response){
       // 1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        // redis中的验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (codeInRedis == null || !codeInRedis.equals(validateCode)){
            // 验证码不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
        Member member = memberService.findByTelephone(telephone);
        if (member == null){
            // 自动注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.save(member);
        }
       // 3、向客户端写入Cookie，内容为用户手机号
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);
        response.addCookie(cookie);
       // 4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
        String json = JSON.toJSON(member).toString();
        jedisPool.getResource().setex(telephone,60*30,json);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
