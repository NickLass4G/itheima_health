package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckGroup;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/17 19:44
 */
public interface CheckGroupService {
    void save(CheckGroup checkGroup, Integer[] checkItemIds);

    PageResult findByPage(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup findCheckGroupById(int id);

    List<Integer> findCheckItemIdByCheckGroupId(int checkGroupId);

    void update(CheckGroup checkGroup, Integer[] checkItemIds);

    void deleteById(int id);
}
