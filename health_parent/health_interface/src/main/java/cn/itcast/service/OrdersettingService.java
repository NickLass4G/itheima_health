package cn.itcast.service;

import cn.itcast.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/19 22:21
 */
public interface OrdersettingService {
    void save(List<OrderSetting> orderSettingList);

    List<Map> findByMonth(String date);

    void updateNumberByDate(OrderSetting orderSetting);
}
