package cn.itcast.mapper;

import cn.itcast.pojo.Member;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/22 21:29
 */
public interface MemberMapper {
    Member findByTel(String telephone);

    void save(Member m);

    Integer findMemberCountBeforeDate(String m);

    Long findTodayNewMember(String today);

    Long findTotalMember();

    Long findThisWeekNewMember(String monday);

    Long findThisMonthNewMember(String firstDay);

    Page<Member> findPage(String queryString);

    void deleteMemberById(int id);

    Member findMemberById(int id);

    void update(Member member);

    List<Member> findMemberByIds(Integer[] ids);
}
