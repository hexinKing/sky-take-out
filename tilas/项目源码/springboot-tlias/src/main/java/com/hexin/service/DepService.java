package com.hexin.service;

import com.hexin.pojo.Dept;

import java.util.List;

public interface DepService {

    //    部门列表查询
    public List<Dept> DepList();

    //    删除部门
    public void DepDelete(Integer id);

    //    添加部门
    public void DepAdd(Dept dept);

    //    根据ID查询
    public Dept DepIdList(Integer id);

    //    修改部门
    public void DepUpdate(Dept dept);
}
