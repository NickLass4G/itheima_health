package cn.itcast.mapper;

import cn.itcast.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @Author:Administrator
 * @Date: 2019/11/17 19:58
 */
public interface CheckGroupMapper {
    void save(CheckGroup checkGroup);

    void saveCheckGroupAndCheckItem(Map<String, Integer> hashMap);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findCheckGroupById(int id);

    List<Integer> findCheckItemIdByCheckGroupId(int checkGroupId);

    void updateCheckGroup(CheckGroup checkGroup);

    void deleteCheckGroupAndCheckItem(Integer id);

    void deleteCheckGroupById(int id);
}
