package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/17 19:36
 */
@RestController
        @RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 1.增检查组
     * @param checkGroup
     * @param checkItemIds
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody CheckGroup checkGroup , Integer[] checkItemIds){
        try {
            checkGroupService.save(checkGroup,checkItemIds);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 2.检查组分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.findByPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString().replaceAll(" ",""));
    }

    /**
     * 3.根据id查询检查组
     * @param id
     * @return
     */
    @RequestMapping("/findGroupById")
    public Result findGroupById(int id){
        CheckGroup checkGroup = checkGroupService.findCheckGroupById(id);
        if (checkGroup != null){
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }
        return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    /**
     * 4.根据检查组id去关联表查询所有检查项的id
     * @param checkGroupId
     * @return
     */
    @RequestMapping("/findCheckItemIdByCheckGroupId")
    public Result findCheckItemIdByCheckGroupId(int checkGroupId){
        List<Integer> checkItemIdList = null;
        try {
            checkItemIdList = checkGroupService.findCheckItemIdByCheckGroupId(checkGroupId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIdList);
    }

    /**
     * 5.修改检查组
     * @param checkGroup
     * @param checkItemIds
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup , Integer[] checkItemIds){
        try {
            checkGroupService.update(checkGroup,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 6.删除检查组
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result deleteById(int id){
        try {
            checkGroupService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
