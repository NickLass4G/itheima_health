package cn.itcast.service;

import cn.itcast.mapper.MemberMapper;
import cn.itcast.pojo.Member;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    public void save(Member member) {
        memberMapper.save(member);
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
}
