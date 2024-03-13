package com.hexin.service.impl;

import com.hexin.mapper.DepMapper;
import com.hexin.mapper.EmpMapper;
import com.hexin.pojo.Dept;
import com.hexin.service.DepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DepServiceImpl implements DepService {

    @Autowired
    private DepMapper depMapper;

    @Autowired
    private EmpMapper empMapper;

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<Dept> DepList() {
        List<Dept> depts = depMapper.DepList();
        return depts;
    }

    /**
     * 根据id删除部门，再删除部门时，对应的员工表数据同时删除
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)//添加事务,默认情况下只有运行时异常才会建立事务
    // rollbackFor = Exception.class表示不管任何异常都会建立事务
    @Override
    public void DepDelete(Integer id) {
        depMapper.DepDelete(id);
        empMapper.deptIdDelete(id);
    }

    /**
     * 添加
     *
     * @param dept
     */
    @Override
    public void DepAdd(Dept dept) {
//        在业务逻辑层补全数据
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        depMapper.DepAdd(dept);
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public Dept DepIdList(Integer id) {
        Dept depts = depMapper.DepIdList(id);
        return depts;
    }

    /**
     * 修改
     *
     * @param dept
     */
    @Override
    public void DepUpdate(Dept dept) {
        //      更新修改时间
        dept.setUpdateTime(LocalDateTime.now());
        depMapper.DepUpdate(dept);

    }
}
