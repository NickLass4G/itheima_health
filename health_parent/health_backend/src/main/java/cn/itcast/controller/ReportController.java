package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.*;
import cn.itcast.service.*;
import cn.itcast.utils.DateUtils;
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

    @Reference
    private OrderService orderService;

    @Reference
    private CheckGroupService checkGroupService;

    @Reference
    private CheckItemService checkItemService;

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


    @RequestMapping("/exportMemberDetailInformation")
    public Result exportMemberDetailInformation(Integer[] ids,HttpServletRequest request, HttpServletResponse response)throws Exception{
        // 查询出所有勾选的会员

        try {
            int memberJump = 2;//会员跳的行数,就是所有订单的检查项加起来的行数
            // 获取Excel文件的绝对路径
            // File.separator是文件夹与文件之间分隔符,由于Linux与Windows分隔符不同,这个东西可以自己根据系统进行取值
            String templateRealPath = request.getSession().getServletContext().getRealPath("template")+ File.separator + "member_detail_template.xlsx";
            // 读取模板文件,创建Excel表格对象并写入数据
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = null;
            List<Member> memberList = memberService.findByIds(ids);
            for (Member member : memberList) {
                // 1.1将会员信息写进Excel sheet0
                row = sheet.getRow(memberJump);
                row.getCell(0).setCellValue(""+member.getFileNumber());
                row.getCell(1).setCellValue(""+member.getName());
                row.getCell(2).setCellValue((""+member.getSex()).equals("1")?"男":"女");
                row.getCell(3).setCellValue(""+member.getIdCard());
                row.getCell(4).setCellValue(""+member.getPhoneNumber());
                row.getCell(5).setCellValue(member.getRegTime()==null?null+"":DateUtils.parseDate2String(member.getRegTime()));
                row.getCell(6).setCellValue(""+member.getPassword());
                row.getCell(7).setCellValue(""+member.getEmail());
                row.getCell(8).setCellValue(member.getBirthday()==null?null+"":DateUtils.parseDate2String(member.getBirthday()));
                row.getCell(9).setCellValue(""+member.getRemark());

                // 1.2
                List<Order> orderList = orderService.findByMemberId(member.getId());
                if (orderList == null || orderList.size() <= 0){
                    memberJump += 1;
                    continue;
                }
                int i = 0;
                int s= 0;
                for (Order order : orderList) {
                    row = sheet.getRow(memberJump+i++);
                    row.getCell(11).setCellValue(order.getOrderDate()==null?null+"":DateUtils.parseDate2String(order.getOrderDate()));
                    row.getCell(12).setCellValue(""+order.getOrderType());
                    row.getCell(13).setCellValue(""+order.getOrderStatus());

                    Setmeal setmeal = setmealService.findByOrderId(order.getId());
                    row = sheet.getRow(memberJump+s++);
                    row.getCell(14).setCellValue(""+setmeal.getName());
                    row.getCell(15).setCellValue(""+setmeal.getCode());
                    row.getCell(16).setCellValue(""+setmeal.getHelpCode());
                    row.getCell(17).setCellValue((""+setmeal.getSex()).equals("1")?"男":"女");
                    row.getCell(18).setCellValue(""+setmeal.getAge());
                    row.getCell(19).setCellValue(""+setmeal.getPrice());
                    row.getCell(20).setCellValue(""+setmeal.getRemark());
                    row.getCell(21).setCellValue(""+setmeal.getAttention());
                    row.getCell(22).setCellValue(""+setmeal.getImg());

                    int cgl = 0;
                    //
                    List<CheckGroup> checkGroupList = checkGroupService.findBySetmealId(setmeal.getId());

                    for (CheckGroup checkGroup : checkGroupList) {
                        row = sheet.getRow(memberJump+cgl++);
                        row.getCell(23).setCellValue(""+checkGroup.getCode());
                        row.getCell(24).setCellValue(""+checkGroup.getName());
                        row.getCell(25).setCellValue(""+checkGroup.getHelpCode());
                        row.getCell(26).setCellValue((""+setmeal.getSex()).equals("1")?"男":"女");;
                        row.getCell(27).setCellValue(""+checkGroup.getRemark());
                        row.getCell(28).setCellValue(""+checkGroup.getAttention());

                        int cil = 0;
                        List<CheckItem> checkItemList = checkItemService.findByCheckGroupId(checkGroup.getId());
                        memberJump += checkItemList.size();
                        for (CheckItem checkItem : checkItemList) {
                            row = sheet.getRow(memberJump+cil++);
                            row.getCell(29).setCellValue(""+checkItem.getCode());
                            row.getCell(30).setCellValue(""+checkItem.getName());
                            row.getCell(31).setCellValue((""+checkItem.getSex()).equals("1")?"男":"女");
                            row.getCell(32).setCellValue(""+checkItem.getAge());
                            row.getCell(33).setCellValue(""+checkItem.getPrice());
                            row.getCell(34).setCellValue((""+checkItem.getType()).equals("1")?"检查":"化验");

                            row.getCell(35).setCellValue(""+checkItem.getAttention());
                            row.getCell(36).setCellValue(""+checkItem.getRemark());
                        }
                    }
                }
            }

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
            return new Result(false,"无所谓...");
        }



    }
}
