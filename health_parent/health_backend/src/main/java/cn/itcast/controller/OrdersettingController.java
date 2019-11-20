package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrdersettingService;
import cn.itcast.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/19 21:50
 */
@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {
    @Reference
    private OrdersettingService ordersettingService;

    /**
     * 1.解析上传的文件并写到数据库
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            List<String[]> excelList = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingList = new ArrayList<>(excelList.size()+1);
            // 遍历excelList集合,得到String[]
            for (String[] rows : excelList) {
                // 遍历String[],String[0]为日期,String[1]为可预约数量
                //String dateStr = rows[0];
                //String numStr = rows[1];
                // 处理一下日期和数量
               // Date date = new Date(dateStr);
                //int num = Integer.parseInt(numStr);
                // 创建OrderSetting对象
                //OrderSetting orderSetting = new OrderSetting(date,num);
                // 填充orderSettingList集合
                //orderSettingList.add(orderSetting);
                orderSettingList.add(new OrderSetting(new Date(rows[0]),Integer.parseInt(rows[1])));
            }
            // 调用mapper写入数据库
            ordersettingService.save(orderSettingList);
            // return true
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 2.根据一个日期类型的字符串查询这个月的预约数据
     * @param date
     * @return
     */
    @RequestMapping("/findByMonth")
    public Result findByMonth(String date){//2019-3
        try {
            List<Map> list = ordersettingService.findByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 3.根据日期修改可预约人数
     * 接收json格式字符串封装为对象
     * @return
     */
    @RequestMapping("/updateNumberByDate")
    public Result updateNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            ordersettingService.updateNumberByDate(orderSetting);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }
}
