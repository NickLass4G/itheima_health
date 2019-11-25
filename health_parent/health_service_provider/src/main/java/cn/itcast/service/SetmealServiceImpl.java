package cn.itcast.service;

import cn.itcast.constant.RedisConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.mapper.SetmealMapper;
import cn.itcast.pojo.Setmeal;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/19 14:23
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private JedisPool jedisPool;
    /**
     * 1.新建套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void save(Setmeal setmeal, Integer[] checkGroupIds) {
        // 1.新建套餐记录,注意要返回主键的值
        setmealMapper.save(setmeal);
        // 2.在关联表中插入数据
        saveSetmealAndCheckGroup(setmeal.getId(),checkGroupIds);
        // 优化文件上传,将点击保存后的文件名保存到Redis
        savePic2Redis(setmeal.getImg());
    }

    /**
     * 2.套餐分页
     * @param currentPage
     * @param pageSize
     * @param s
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String s) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page = setmealMapper.findPage(s);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 移动端后台1:查询所有套餐
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    /**
     * 移动端后台2:根据id查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {

        return setmealMapper.findById(id);
    }

    /**
     * 后台管理端:
     * 查询套餐预定的数量统计
     * 用于饼图
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealMapper.findSetmealCount();
    }

    // 将点击保存后的文件名保存到Redis
    private void savePic2Redis(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    // 封装插入关联表数据的方法
    private void saveSetmealAndCheckGroup(Integer id, Integer[] checkGroupIds) {
        if (checkGroupIds != null && checkGroupIds.length > 0 ){
            int size = checkGroupIds.length;
            for (Integer checkGroupId : checkGroupIds) {
                Map<String,Integer> map = new HashMap<>(size);
                map.put("setmeal_id",id);
                map.put("checkgroup_id",checkGroupId);
                setmealMapper.saveSetmealAndCheckGroup(map);
            }
        }
    }
}
