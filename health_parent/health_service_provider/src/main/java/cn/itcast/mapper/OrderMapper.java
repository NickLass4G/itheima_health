package cn.itcast.mapper;

import cn.itcast.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/22 20:56
 */
public interface OrderMapper {
    List<Order> findByCondition(Order order);

    void save(Order order);

    Map fidnById(int id);
}
