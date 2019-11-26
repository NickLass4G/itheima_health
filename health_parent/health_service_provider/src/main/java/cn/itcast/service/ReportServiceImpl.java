package cn.itcast.service;

import cn.itcast.mapper.MemberMapper;
import cn.itcast.mapper.OrderMapper;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/25 14:08
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 1.运营情况报表
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        /*
        * reportDate:null,
                    todayNewMember :0,
                    totalMember :0,
                    thisWeekNewMember :0,
                    thisMonthNewMember :0,
                    todayOrderNumber :0,
                    todayVisitsNumber :0,
                    thisWeekOrderNumber :0,
                    thisWeekVisitsNumber :0,
                    thisMonthOrderNumber :0,
                    thisMonthVisitsNumber :0,*/
        Map<String, Object> map = new HashMap<>(20);
        // 今天
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        // 本周一
        String Monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        // 本月一号
        String firstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        // reportDate
        map.put("reportDate",today);
        // todayNewMember
        Long todayNewMember = memberMapper.findTodayNewMember(today);
        map.put("todayNewMember",todayNewMember);
        // totalMember
        Long totalMember = memberMapper.findTotalMember();
        map.put("totalMember",totalMember);
        // thisWeekNewMember
        Long thisWeekNewMember = memberMapper.findThisWeekNewMember(Monday);
        map.put("thisWeekNewMember",thisWeekNewMember);
        // thisMonthNewMember
        Long thisMonthNewMember = memberMapper.findThisMonthNewMember(firstDay);
        map.put("thisMonthNewMember",thisMonthNewMember);
        // todayOrderNumber
        Long todayOrderNumber = orderMapper.findTodayOrderNumber(today);
        map.put("todayOrderNumber",todayOrderNumber);
        // todayVisitsNumber
        Long todayVisitsNumber = orderMapper.findTodayVisitsNumber(today);
        map.put("todayVisitsNumber",todayVisitsNumber);
        // thisWeekOrderNumber
        Long thisWeekOrderNumber = orderMapper.findOrderNumberAfterDate(Monday);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        // thisWeekVisitsNumber
        Long thisWeekVisitsNumber = orderMapper.findVisitsNumberAfterDate(Monday);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        // thisMonthOrderNumber
        Long thisMonthOrderNumber = orderMapper.findOrderNumberAfterDate(firstDay);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        // thisMonthVisitsNumber
        Long thisMonthVisitsNumber = orderMapper.findVisitsNumberAfterDate(firstDay);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        // hotSetmeal
        List<Map<String,Object>> hotSetmeal = orderMapper.findHotSetmeal();
        map.put("hotSetmeal",hotSetmeal);
        return map;
    }
}
