package cn.itcast.service;

import cn.itcast.entity.Result;
import cn.itcast.pojo.Order;

import java.util.List;
import java.util.Map; /**
 * @Author:Administrator
 * @Date: 2019/11/22 20:48
 */
public interface OrderService {
    Result order(Map map) throws Exception;

    Map findById(int id) throws Exception;

    List<Order> findByMemberId(int memberId);
}
