package cn.itcast.service;

import cn.itcast.pojo.CheckGroup; /**
 * @Author:Administrator
 * @Date: 2019/11/17 19:44
 */
public interface CheckGroupService {
    void save(CheckGroup checkGroup, Integer[] checkItemIds);
}
