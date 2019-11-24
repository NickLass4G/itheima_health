package cn.itcast.service;

import cn.itcast.mapper.MemberMapper;
import cn.itcast.pojo.Member;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
