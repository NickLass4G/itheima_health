package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author:Administrator
 * @Date: 2019/11/20 21:04
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    /**
     * 移动端1:查询所有
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Setmeal> setmealList = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 移动端:2.根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal = setmealService.findById(id);

            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
