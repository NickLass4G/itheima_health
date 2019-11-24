package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Order;
import cn.itcast.service.OrderService;
import cn.itcast.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/22 16:16
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 1.用户提交预约
     * @return
     */
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        String telephone = (String) map.get("telephone");// 先获取用户输入的手机号码
        // 1/从Redis中获取验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        // 2.校验手机验证码
        String validateCode = (String) map.get("validateCode");// 用户输入的验证码
        if (codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 3.调用体检预约服务
        map.put("orderType", Order.ORDERTYPE_WEIXIN); // 设置一下预约类型,后来能用到
        Result result = null;
        try {
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            // 预约失败
            return result;
        }
        // 5.预约成功,发送短信通知
        if (result.isFlag()){
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
