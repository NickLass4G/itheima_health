package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.pojo.Setmeal; /**
 * @Author:Administrator
 * @Date: 2019/11/19 14:22
 */
public interface SetmealService {
    void save(Setmeal setmeal, Integer[] checkGroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String s);
}
