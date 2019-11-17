package cn.itcast.mapper;

import cn.itcast.pojo.CheckGroup;

import java.util.Map; /**
 * @Author:Administrator
 * @Date: 2019/11/17 19:58
 */
public interface CheckGroupMapper {
    void save(CheckGroup checkGroup);

    void saveCheckGroupAndCheckItem(Map<String, Integer> hashMap);
}
