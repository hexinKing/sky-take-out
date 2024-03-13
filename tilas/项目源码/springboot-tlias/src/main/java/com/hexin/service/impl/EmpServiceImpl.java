package com.hexin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hexin.mapper.EmpMapper;
import com.hexin.pojo.Emp;
import com.hexin.pojo.PageBean;
import com.hexin.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

//    原始分页方法
//    @Override
//    public PageBean EmpPaginationList(Integer page, Integer pageSize) {
////        获取总信息数
//        Long count = empMapper.count();
////        对page进行处理
//        Integer data=(page-1)*pageSize;
////        获取分页后的数据
//        List<Emp> emps = empMapper.EmpPaginationList(data, pageSize);
//
////        对总信息数和分页后的数据进行封装，封装到PageBean实体类中
//        PageBean pageBean = new PageBean(count,emps);
//
//        return pageBean;
//
//    }

////    使用pagehelper分页插件
//    @Override
//    public PageBean EmpPaginationList(Integer page, Integer pageSize) {
////        1.设置分页参数
//        PageHelper.startPage(page,pageSize);
////        2.执行查询
//        List<Emp> list = empMapper.list();
//        Page<Emp> p = (Page<Emp>) list;
////        3.封装PageBean对象
//        PageBean pageBean = new PageBean(p.getTotal(),p.getResult());
//        return pageBean;
//
//    }

    /**
     * 分页条件查询
     *
     * @param name
     * @param gender
     * @param begin
     * @param end
     * @param page
     * @param pageSize
     * @return
     */

    //    使用pagehelper分页插件，实现条件分页查询,将分页交给插件处理
    @Override
    public PageBean EmpList(String name, Short gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {

//        1.设置分页参数
        PageHelper.startPage(page, pageSize);
//        2.执行查询
        List<Emp> list = empMapper.EmpList(name, gender, begin, end);
        Page<Emp> p = (Page<Emp>) list;
//        3.封装PageBean对象
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;

    }


    /**
     * 删除员工
     *
     * @param ids
     */
    @Override
    public void EmpDelete(List<Integer> ids) {
        empMapper.EmpDelete(ids);
    }

    /**
     * 添加员工
     *
     * @param emp
     */

    @Override
    public void EmpAdd(Emp emp) {
        //        在业务逻辑层补全数据
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.EmpAdd(emp);
    }

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @Override
    public Emp EmpIdList(Integer id) {
        Emp emps = empMapper.EmpIdList(id);
        return emps;
    }

    /**
     * 修改员工
     *
     * @return
     */
    @Override
    public void EmpUpdate(Emp emp) {
        empMapper.EmpUpdate(emp);
//      更新修改时间
        emp.setUpdateTime(LocalDateTime.now());

    }

    /**
     * 系统登录
     *
     * @param emp
     * @return
     */
    @Override
    public Emp SystemLogin(Emp emp) {
        Emp emp1 = empMapper.SystemLogin(emp);
        return emp1;
    }
}
