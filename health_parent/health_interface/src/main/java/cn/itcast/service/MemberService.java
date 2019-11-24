package cn.itcast.service;

import cn.itcast.pojo.Member;

/**
 * @Author:Administrator
 * @Date: 2019/11/23 14:04
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void save(Member member);
}
