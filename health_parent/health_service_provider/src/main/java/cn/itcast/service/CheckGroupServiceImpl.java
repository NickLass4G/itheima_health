package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.mapper.CheckGroupMapper;
import cn.itcast.pojo.CheckGroup;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author:Administrator
 * @Date: 2019/11/17 19:45
 * 检查组Service
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;
    /**
     * 1.新建检查组
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    public void save(CheckGroup checkGroup, Integer[] checkItemIds) {
        //1.添加checkgroup表中数据,同时添加checkgroup_checkitem表中数据
        checkGroupMapper.save(checkGroup);
        saveCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);
    }

    /**
     * 2.检查组分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findByPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page = checkGroupMapper.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 3.根据id查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findCheckGroupById(int id) {
        return checkGroupMapper.findCheckGroupById(id);
    }

    /**
     * 4.根据检查组id去关联表查询所有检查项的id
     * @param checkGroupId
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdByCheckGroupId(int checkGroupId) {
        return checkGroupMapper.findCheckItemIdByCheckGroupId(checkGroupId);
    }

    /**
     * 5.修改检查组
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    public void update(CheckGroup checkGroup, Integer[] checkItemIds) {

        //2.删除关联表中的信息,这里的checkgroup是带id的
        checkGroupMapper.deleteCheckGroupAndCheckItem(checkGroup.getId());
        //3.新建关联表中的数据
        saveCheckGroupAndCheckItem(checkGroup.getId(),checkItemIds);
        //1.修改检查组表中的信息
        checkGroupMapper.updateCheckGroup(checkGroup);
    }

    /**
     * 6.删除检查组
     * @param id
     */
    @Override
    public void deleteById(int id) {
        //1.删除关联表中的数据
        checkGroupMapper.deleteCheckGroupAndCheckItem(id);
        //2.删除检查组数据
        checkGroupMapper.deleteCheckGroupById(id);
    }

    /**
     * 7.查询所有检查组
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }

    /**
     * 8.根据套餐id查询检查组
     * @param setmealId
     * @return
     */
    @Override
    public List<CheckGroup> findBySetmealId(Integer setmealId) {
        return checkGroupMapper.findCheckGroupBySetmeaId(setmealId);
    }

    /*
    * 新建检查组时同时向关联表中插入记录
    * */
    public void saveCheckGroupAndCheckItem(Integer id, Integer[] checkItemIds) {
        //判断数组长度
        if (checkItemIds != null && checkItemIds.length > 0){

            //创建一个HashMap
            Map<String,Integer> hashMap = new HashMap<>();
            //遍历数组,构建map集合的数据
            for (Integer checkItemId : checkItemIds) {

                hashMap.put("checkgroup_id",id);
                hashMap.put("checkitem_id",checkItemId);
                //调用mapper,传map过去
                checkGroupMapper.saveCheckGroupAndCheckItem(hashMap);
                hashMap.clear();
            }
        }
    }
}
