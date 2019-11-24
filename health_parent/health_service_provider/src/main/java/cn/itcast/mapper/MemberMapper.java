package cn.itcast.mapper;

import cn.itcast.pojo.Member;

/**
 * @Author:Administrator
 * @Date: 2019/11/22 21:29
 */
public interface MemberMapper {
    Member findByTel(String telephone);

    void save(Member m);
}
