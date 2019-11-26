package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.service.MemberService;
import cn.itcast.service.ReportService;
import cn.itcast.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:Administrator
 * @Date: 2019/11/25 9:34
 * 统计报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 1.会员数量折线图
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);// 获取当前日期之前12个月的日期

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("months",list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 2.套餐报告饼图
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            // setmealCount里面是个套着map的list
            List<Map<String,Object>> list = setmealService.findSetmealCount();
            Map<String,Object> map = new HashMap<>();
            map.put("setmealCount",list);

            // 名字,在setmealCount里面封装了有
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> m : list) {
                String name = (String) m.get("name");
                setmealNames.add(name);
            }
            map.put("setmealNames",setmealNames);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 3.运营情况报表
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 4.报表导出
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 调用远程服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();
            // 取出数据,准备写入到Excel
            String reportDate = (String) result.get("reportDate");
            Long todayNewMember = (Long) result.get("todayNewMember");
            Long totalMember = (Long) result.get("totalMember");
            Long thisWeekNewMember = (Long) result.get("thisWeekNewMember");
            Long thisMonthNewMember = (Long) result.get("thisMonthNewMember");
            Long todayOrderNumber = (Long) result.get("todayOrderNumber");
            Long todayVisitsNumber = (Long) result.get("todayVisitsNumber");
            Long thisWeekOrderNumber = (Long) result.get("thisWeekOrderNumber");
            Long thisWeekVisitsNumber = (Long) result.get("thisWeekVisitsNumber");
            Long thisMonthOrderNumber = (Long) result.get("thisMonthOrderNumber");
            Long thisMonthVisitsNumber = (Long) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            // 获取Excel文件的绝对路径
            // File.separator是文件夹与文件之间分隔符,由于Linux与Windows分隔符不同,这个东西可以自己根据系统进行取值
            String templateRealPath = request.getSession().getServletContext().getRealPath("template")+ File.separator + "report_template.xlsx";
            // 读取模板文件,创建Excel表格对象并写入数据
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);// 日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember); // 今日新增会员数
            row.getCell(7).setCellValue(totalMember); // 总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember); // 本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember); // 本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber); // 今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber); // 今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber); // 本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber); // 本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber); // 本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber); // 本月到诊数

            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmealCount = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmealCount);
                row.getCell(6).setCellValue(proportion.doubleValue());
            }
            // 通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content‐Disposition","attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }
}
