package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //对客服端传过来的密码进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //getBytes():将字符串转换为字节数组的方法

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void AddEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
//       属性对象拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
//        设置默认密码,并对密码进行MD5加密
        String password=PasswordConstant.DEFAULT_PASSWORD;
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
//        设置默认账号状态
        employee.setStatus(StatusConstant.ENABLE);
//        设置创建时间和最后修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
//        从threadLocal中获取当前操作的员工ID
        Long empID = BaseContext.getCurrentId();
//        设置创建人id和最后修改人id
        employee.setCreateUser(empID);
        employee.setUpdateUser(empID);

        employeeMapper.AddEmployee(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult EmployeePageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 设置分页参数
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        // 执行分页查询
        Page<Employee> page = employeeMapper.EmployeePageQuery(employeePageQueryDTO);
        // 获取分页结果
        long total = page.getTotal();//总记录数
        List<Employee> records = page.getResult();//当前页数据集合
        //封装PageResult
        PageResult pageResult = new PageResult(total, records);
        //放回结果
        return pageResult;
    }

}
