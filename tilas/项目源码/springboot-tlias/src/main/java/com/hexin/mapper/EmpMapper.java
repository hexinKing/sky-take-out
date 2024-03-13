package com.hexin.mapper;

import com.hexin.pojo.Emp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface EmpMapper {

    //    员工列表条件分页查询
    public List<Emp> EmpList(String name, Short gender, LocalDate begin, LocalDate end);

    //    删除员工
    void EmpDelete(List<Integer> ids);

    //    根据部门id删除员工
    @Delete("delete from emp where dept_id=#{deptId}")
    void deptIdDelete(Integer deptId);


    //    添加员工
    void EmpAdd(Emp emp);

    //    根据id查询
    @Select("select * from emp where id=#{id}")
    Emp EmpIdList(Integer id);

    //    修改员工
    void EmpUpdate(Emp emp);

    //    系统登录
    @Select("select * from emp where username=#{username} and password=#{password}")
    Emp SystemLogin(Emp emp);

//    ----------原始分页方法
//    //    查询信息总条数
//    @Select("select COUNT(*) from emp")
//    Long count();
//
//    //    员工列表分页查询
//    @Select("select * from emp limit #{page}, #{pageSize}")
//    List<Emp> EmpPaginationList(Integer page, Integer pageSize);

//       员工列表分页查询
//     ---------使用pagehelper分页插件
//    @Select("select * from emp")
//    public List<Emp> list();


}
