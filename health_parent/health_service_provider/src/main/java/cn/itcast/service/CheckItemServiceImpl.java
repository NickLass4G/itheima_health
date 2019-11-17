package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.mapper.CheckItemMapper;
import cn.itcast.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/16 21:10
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemMapper checkItemMapper;

    /**
     * 1.新增检查项
     *
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem){
        checkItemMapper.add(checkItem);
    }

    /**
     * 2.分页查询
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString){
        int _currentPage = currentPage;
        int _pageSize = pageSize;
        PageHelper.startPage(_currentPage, _pageSize);
        Page<CheckItem> page = checkItemMapper.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 3.根据id删除检查项
     *
     * @param id
     */
    @Override
    public void deleteItem(int id){
        //检查关联表中是否有记录
        long count = checkItemMapper.findCountByCheckItemId(id);
        if (count > 0){
            //关联表中有记录,不能删除

            throw new RuntimeException("当前检查项被引用,不能删除!");
        }
        // 无记录,进行删除
        checkItemMapper.deleteItem(id);
    }

    /**
     * 4.根据id查询检查项
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemMapper.findById(id);
    }

    /**
     * 5.改checkitem
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemMapper.updateCheckItem(checkItem);
    }

    /**
     * 6.c查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemMapper.findAll();
    }
}
