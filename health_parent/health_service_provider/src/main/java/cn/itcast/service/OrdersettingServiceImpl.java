package cn.itcast.service;

import cn.itcast.mapper.OrdersettingMapper;
import cn.itcast.pojo.OrderSetting;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/19 22:23
 */
@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {

    @Autowired
    private OrdersettingMapper ordersettingMapper;
    /**
     * 1.将转化后的Excel文件数据写到数据库
     * @param orderSettingList
     */
    @Override
    public void save(List<OrderSetting> orderSettingList) {
        //ordersettingMapper.save(orderSettingList);不能直接进行插入数据的操作
        if (orderSettingList != null && orderSettingList.size() > 0){
            for (OrderSetting orderSetting : orderSettingList) {
                // 根据日期查询记录数
                long count = ordersettingMapper.findorderSettingCountByDate(orderSetting.getOrderDate());
                if (count > 0 ){
                    // 有记录执行更新操作
                    ordersettingMapper.updateByDate(orderSetting);
                }else {
                    // 无记录执行新增操作
                    ordersettingMapper.save(orderSetting);
                }
            }
        }
    }

    /**
     * 2.根据一个日期类型的字符串查询一个月
     * @param date
     * @return
     */
    @Override
    public List<Map> findByMonth(String date) {
        // 1.构建月初月尾字符串
        String dateBegin = date + "-1";
        String dataEnd = date + "-31";
        // 2.构建查询的map集合
        Map map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dataEnd);
        // 3.查询返回list<ordersetting>集合
        List<OrderSetting> orderSettingList = ordersettingMapper.findByMonth(map);
        // 4.定义List<Map>集合 遍历list<ordersetting>集合,为其填充内容
        List<Map> list = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map orderSettingMap = new HashMap();
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            list.add(orderSettingMap);
        }
        return list;
    }

    /**
     * 3.根据日期修改可预约的人数
     * @param orderSetting
     */
    @Override
    public void updateNumberByDate(OrderSetting orderSetting) {
        // 1.查询当前日期是否被预约
        long count = ordersettingMapper.findorderSettingCountByDate(orderSetting.getOrderDate());
        // 2.已被预约,进行修改操作
        if (count > 0){
            ordersettingMapper.updateByDate(orderSetting);
        }else {
            ordersettingMapper.save(orderSetting);
        }
        // 3.没被预约进行添加操作
    }
}
