package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Order;
import cn.itcast.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/26 20:56
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;

    /**
     * 1.根据会员的id查询套餐
     * @param memberId
     * @return
     */
    @RequestMapping("/findByMemberId")
    public Result findByMemberId(int memberId){
        try {
            List<Order> orderList = orderService.findByMemberId(memberId);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
