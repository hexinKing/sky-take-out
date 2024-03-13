package com.hexin.contorller;

import com.hexin.anno.Log;
import com.hexin.pojo.Dept;
import com.hexin.pojo.Result;
import com.hexin.service.DepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 部门管理
 */
@RestController
@RequestMapping("/depts")
public class DepContorller {

    @Autowired
    private DepService depService;

    //    部门列表查询
//    @RequestMapping(value = "/depts" ,method = RequestMethod.GET)
    @GetMapping
    public Result DepList() {
        List<Dept> depts = depService.DepList();
        return Result.success(depts);
    }


    //    删除部门
    @Log
    @DeleteMapping("/{id}")
    public Result DepDelete(@PathVariable Integer id) {//@PathVariable：获取客服端/depts/{id}中的id值
//        @PathVariable 路径参数
        depService.DepDelete(id);
        return Result.success();
    }


    //    添加部门
    @Log
    @PostMapping
    public Result DepAdd(@RequestBody Dept dept) {//@RequestBody：获取客服端json格式的数据
        //@RequestBody json参数
        depService.DepAdd(dept);
        return Result.success();
    }


    //    根据ID查询
    @GetMapping("/{id}")
    public Result DepIdList(@PathVariable Integer id) {
        Dept depts = depService.DepIdList(id);
        return Result.success(depts);
    }


    //     修改部门
    @Log
    @PutMapping
    public Result DepUpdate(@RequestBody Dept dept) {
        depService.DepUpdate(dept);
        return Result.success();
    }

}
