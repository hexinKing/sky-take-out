package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void AddEmployee(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult EmployeePageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    /**
     * 更新启用、禁用员工账号
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, Long id);


    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);


    /**
     * 编辑员工数据
     * @param employeeDTO
     */
    void updateEmp(EmployeeDTO employeeDTO);


    /**
     * 修改密码
     * @param passwordEditDTO
     */
    void changePass(PasswordEditDTO passwordEditDTO);

}
