package cn.itcast.service;

import cn.itcast.mapper.CheckGroupMapper;
import cn.itcast.pojo.CheckGroup;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
