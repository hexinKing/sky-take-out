package com.hexin.contorller;


import com.hexin.anno.Log;
import com.hexin.pojo.Emp;
import com.hexin.pojo.PageBean;
import com.hexin.pojo.Result;
import com.hexin.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpContorller {

    @Autowired
    private EmpService empService;

//    员工列表分页查询
//    @GetMapping
//    public Result EmpPaginationList(@RequestParam(defaultValue = "1") Integer page ,
//                                    @RequestParam(defaultValue = "10") Integer pageSize ){
//        PageBean pageBean = empService.EmpPaginationList(page,pageSize);
//        return Result.success(pageBean);
//    }


    //    员工列表条件分页查询
    @GetMapping
    public Result EmpList(String name, Short gender,
                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
//                          @DateTimeFormat(pattern = "yyyy-MM-dd") 时间参数
                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
                          @RequestParam(defaultValue = "1") Integer page,
//                          @RequestParam(defaultValue = "1") 设置默认值，若参数为null时，则对其赋予初始值
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = empService.EmpList(name, gender, begin, end, page, pageSize);
        return Result.success(pageBean);
    }

    //    删除员工
    @Log
    @DeleteMapping("/{ids}")
    public Result EmpDelete(@PathVariable List<Integer> ids) {//@PathVariable:路径参数时使用
//        log.info("客服端传来的id集合：{}" ,ids);
        empService.EmpDelete(ids);
        return Result.success();
    }


    //    添加员工
    @Log
    @PostMapping
    public Result EmpAdd(@RequestBody Emp emp) {
        log.info("客服端传来的数据：{}", emp);
        empService.EmpAdd(emp);
        return Result.success();
    }

    //    根据ID查询
    @GetMapping("/{id}")
    public Result EmpIdList(@PathVariable Integer id) {
        Emp emps = empService.EmpIdList(id);
        return Result.success(emps);
    }

    //    修改员工
    @Log
    @PutMapping
    public Result EmpUpdate(@RequestBody Emp emp) {
        log.info("客服端传来的数据：{}", emp);
        empService.EmpUpdate(emp);
        return Result.success();
    }


}
