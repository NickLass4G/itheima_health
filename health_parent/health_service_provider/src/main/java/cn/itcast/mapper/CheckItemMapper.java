package cn.itcast.mapper;

import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/16 21:12
 */
public interface CheckItemMapper {
    void add(CheckItem checkItem);

    Page<CheckItem> findPage(String queryString);

    //在checkitem的关联表中查checkitem的记录数
    long findCountByCheckItemId(int id);

    void deleteItem(int id);

    CheckItem findById(int id);

    void updateCheckItem(CheckItem checkItem);

    List<CheckItem> findAll();

    List<CheckItem> findByCheckGroupId(Integer checkGroupId);
}
