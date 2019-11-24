package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.utils.SMSUtils;
import cn.itcast.utils.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * @Author:Administrator
 * @Date: 2019/11/22 15:52
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    // 注入jedis
    @Autowired
    private JedisPool jedisPool;

    /**
     * 1.体检预约发送短信验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        // 生成4位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        // 发送短信
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            // 发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为:"+ code);
        // 生成的验证码存到Redis
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
        // 发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 2.快速登录的验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        // 生成4位验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        // 发送短信
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            // 发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为:"+ code);
        // 生成的验证码存到Redis
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
        // 发送成功
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }



}
