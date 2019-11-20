package cn.itcast.mapper;

import cn.itcast.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * @Author:Administrator
 * @Date: 2019/11/19 22:25
 */
public interface OrdersettingMapper {
    void save(OrderSetting orderSetting);

    long findorderSettingCountByDate(Date orderDate);

    void updateByDate(OrderSetting orderSetting);

    List<OrderSetting> findByMonth(Map map);
}
