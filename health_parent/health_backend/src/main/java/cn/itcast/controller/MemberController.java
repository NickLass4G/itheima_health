package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:Administrator
 * @Date: 2019/11/26 10:10
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;

    /**
     * 1.会员分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean)throws Exception{
        return memberService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString().replaceAll(" ",""));
    }

    /**
     * 2.新增会员
     * @param member
     * @return
     */
    @RequestMapping("/add")
    public Result save(@RequestBody Member member , Integer[] setmealIds){
        try {
            memberService.save(member,setmealIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }
        return new Result(true, MessageConstant.ADD_MEMBER_SUCCESS);
    }

    /**
     * 3.删除会员
     * @param id
     * @return
     */
    @RequestMapping("/deleteMember")
    public Result deleteMember(int id){
        try {
            memberService.deleteMember(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_MEMBER_SUCCESS);
    }

    /**
     * 4.根据id查询会员
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Member member = memberService.findById(id);
            return new Result(true,"随便填点什么吧,反正也不显示..",member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"这里该填点啥?");
        }
    }

    /**
     * 5.修改会员信息
     * @param member
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Member member){

        try {
            memberService.update(member);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_MEMBER_SUCCESS);
    }

    /**
     * 6.无论如何都要删除会员信息
     * 先删订单再删会员
     * @param id
     * @return
     */
    @RequestMapping("/deleteMemberByAnyway")
    public Result deleteMemberByAnyway(int id){
        try {
            memberService.deleteMemberByAnyway(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_MEMBER_SUCCESS);
    }

}
