package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.pojo.CheckItem;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/16 21:07
 */
public interface CheckItemService {
    //新增检查项
    void add(CheckItem checkItem);
    //检查项分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);
    //删除检查项
    void deleteItem(int id);
    //根据id查询检查项
    CheckItem findById(int id);
    //修改checkitem
    void update(CheckItem checkItem);
    //查询所有检查项
    List<CheckItem> findAll();
    //根据检查组id查询检查项
    List<CheckItem> findByCheckGroupId(Integer checkGroupId);
}
