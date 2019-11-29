package cn.itcast.mapper;

import cn.itcast.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @Author:Administrator
 * @Date: 2019/11/19 14:30
 */
public interface SetmealMapper {
    void save(Setmeal setmeal);

    void saveSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findPage(String s);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    List<Map<String,Object>> findSetmealCount();

    Setmeal findByOrderId(Integer id);
}
