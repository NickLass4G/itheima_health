package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;

import java.text.ParseException;
import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/23 14:04
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void save(Member member,Integer[] setmealIds);

    List<Integer> findMemberCountByMonth(List<String> month);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString) throws Exception;

    void deleteMember(int id);

    Member findById(int id);

    void update(Member member);

    void deleteMemberByAnyway(int id);

    List<Member> findByIds(Integer[] ids);
}
