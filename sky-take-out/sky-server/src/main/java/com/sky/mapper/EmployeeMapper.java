package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);


    /**
     * 新增员工
     * @param employee
     */
    @AutoFill(value = OperationType.INSERT)
    void AddEmployee(Employee employee);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> EmployeePageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    /**
     * 更新，启用、禁用员工账号
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void StartOrStop(Employee employee);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);


}
