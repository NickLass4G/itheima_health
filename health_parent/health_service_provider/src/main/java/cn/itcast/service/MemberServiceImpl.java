package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.Result;
import cn.itcast.mapper.MemberMapper;
import cn.itcast.mapper.OrderMapper;
import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/23 14:04
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 1.根据电话号码查询会员信息
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        Member mapperByTel = memberMapper.findByTel(telephone);
        return mapperByTel;
    }

    /**
     * 2.新增会员信息
     * @param member
     */
    @Override
    public void save(Member member , Integer[] setmealIds) {
        memberMapper.save(member);
        saveOrderByMemberIdAndAndSetmealId(member.getId(),setmealIds);
    }



    /**
     * 3.根据月份查询会员的数量
     * @param month
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> month) {
        List<Integer> list = new ArrayList<>();
        for (String m : month) {
            m += ".31";// 格式:2019.3.31
            Integer count = memberMapper.findMemberCountBeforeDate(m);
            list.add(count);
        }
        return list;
    }

    /**
     * 4.会员分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception {
        PageHelper.startPage(currentPage,pageSize);
        Page<Member> memberPage = memberMapper.findPage("%"+queryString+"%");
        for (Member member : memberPage) {
            String s = DateUtils.parseDate2String(member.getRegTime());
            member.setRegTime(DateUtils.parseString2Date(s,"yyyy-MM-dd"));
            if (member.getSex() == null){
                member.setSex("0");
            }
            System.out.println(member.getRegTime());
        }
        return new PageResult(memberPage.getTotal(),memberPage.getResult());
    }

    /**
     * 5.删除会员
     * @param id
     */
    @Override
    public void deleteMember(int id) {
        // 据说可以直接删除,要是此没有被外键引用可以删,要是被
        // 引用了会报SQL异常,这里试一下

         memberMapper.deleteMemberById(id);
    }

    /**
     * 6.根据id查询
     * @param id
     * @return
     */
    @Override
    public Member findById(int id) {
        return memberMapper.findMemberById(id);
    }

    /**
     * 7.修改会员信息
     * @param member
     */
    @Override
    public void update(Member member) {
        memberMapper.update(member);
    }

    /**
     * 8.无论如何都要删除会员信息
     * 先删订单再删会员
     * @param id
     */
    @Override
    public void deleteMemberByAnyway(int id) {
        // 删除订单
        orderMapper.deleteByMemberId(id);
        // 删除会员
        memberMapper.deleteMemberById(id);
    }

    /**
     * 9.根据ids去查会员
     * @param ids
     * @return
     */
    @Override
    public List<Member> findByIds(Integer[] ids) {
        return memberMapper.findMemberByIds(ids);
    }

    /**
     * 新建会员信息的时候,如果定了套餐呢,就一并加到订单表中
     * @param id
     * @param setmealIds
     */
    private void saveOrderByMemberIdAndAndSetmealId(Integer id, Integer[] setmealIds) {
        // 如果选了套餐
        if (setmealIds != null && setmealIds.length > 0){
            // 创建一个Order泛型的集合
            //List<Order> orderList = new ArrayList<>();
            // 遍历ids,封装集合
            for (Integer setmealId : setmealIds) {
                Order order = new Order();
                order.setMemberId(id);
                /* 这里的日期很重要,但是前端在新增窗口的第二个第页面差一
                个日历控件显示可预约的日期,这里先不对日期进行封装,后来前端
                要是能写好的话,就在url和方法中的形参中将日期带过来,在这里再
                处理一下本地化到数据库,空一行以示尊敬
                 */

                order.setOrderType("电话预约");
                order.setOrderStatus("未到诊");
                order.setSetmealId(setmealId);
                // 不封集合了
                orderMapper.save(order);
            }
            //orderMapper.
            // 调用orderMapper新增
        }

    }
}
