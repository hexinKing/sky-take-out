package com.hexin.service;

import com.hexin.pojo.Emp;
import com.hexin.pojo.PageBean;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {


    //    员工列表条件分页查询
    PageBean EmpList(String name, Short gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize);

    //    删除员工
    void EmpDelete(List<Integer> ids);

    //    添加员工
    void EmpAdd(Emp emp);

    //    根据id查询
    Emp EmpIdList(Integer id);

    //    修改员工
    void EmpUpdate(Emp emp);

    //    系统登录
    Emp SystemLogin(Emp emp);
}
