package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Administrator
 * @Date: 2019/11/16 20:53
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 1.新增检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        System.out.println(checkItem);
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 2.分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkItemService.findPage(
                    queryPageBean.getCurrentPage(),
                    queryPageBean.getPageSize(),
                    queryPageBean.getQueryString().replaceAll(" ",""));
    }

    /**
     * 3.删除检查项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/deleteItem")
    public Result deleteItem(int id){
        try {
            checkItemService.deleteItem(id);
        } catch (RuntimeException e){
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 4.根据id进行查询用于数据回显
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id){
        CheckItem checkItem = null;
        try {
            checkItem = checkItemService.findById(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItem);
    }

    /**
     * 5.修改检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        try {
            checkItemService.update(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 6.查询所有检查项
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItemList = checkItemService.findAll();
        if (checkItemList != null && checkItemList.size() > 0 ){
            //查询到结果
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemList);
        }
        return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
    }
}
