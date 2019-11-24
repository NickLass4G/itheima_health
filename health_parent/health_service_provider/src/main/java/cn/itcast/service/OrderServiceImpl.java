package cn.itcast.service;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.mapper.MemberMapper;
import cn.itcast.mapper.OrderMapper;
import cn.itcast.mapper.OrdersettingMapper;
import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/22 20:49
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrdersettingMapper ordersettingMapper;
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 1.用户点击预约
     * @param map
     * @return
     */
    @Override
    public Result order(Map map) throws Exception {
        // 1.检查用户预约的日期是否设置了预约设置,没有的话不能预约
        String orderDate = (String) map.get("orderDate"); //这是什么格式的?
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = ordersettingMapper.findOrderSettingByDate(date);
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 2.检查用户预约的日期的预约是否已满,已满则不能进行预约
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if (reservations >= number){
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 3.检查用户是否是重复预约(同一天预约相同的套餐),重复预约不能进行预约
        // 检查当前用户是否为会员,通过手机号码判断
        String telephone = (String) map.get("telephone");
        Member member = memberMapper.findByTel(telephone);
        if (member != null){
            // 是会员,封装成一个order对象进行查询
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,setmealId);
            List<Order> orderList = orderMapper.findByCondition(order);
            if (orderList != null && orderList.size() > 0 ){
                // 已经预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        // 4.检查用户是否为会员.如果是会员则直接进行预约,不是就先自动完成注册再进行预约
        if (member == null){
           // 用户不是会员,那先注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
           memberMapper.save(member);
        }

        // 可以预约,设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        ordersettingMapper.updateByDate(orderSetting);
        // 保存到预约信息表
        Order order = new Order(member.getId(),
                date,
                (String)map.get("orderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String)map.get("setmealId")));
        orderMapper.save(order);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    /**
     * 2.根据id查询一堆信息
     * @param id
     * @return
     */
    @Override
    public Map findById(int id) throws Exception {
        Map map = orderMapper.fidnById(id);
        if (map != null){
            // 转化一下日期类型的格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
