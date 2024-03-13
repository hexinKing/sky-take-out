package com.hexin.mapper;

import com.hexin.pojo.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DepMapper {

    //    部门列表查询
    @Select("select * from dept")
    public List<Dept> DepList();

    //    根据id删除部门
    @Delete(" delete from dept where id=#{id}")
    public void DepDelete(Integer id);

    //    添加部门
    public void DepAdd(Dept dept);


    //    根据id查询
    @Select("select * from dept where id=#{id}")
    public Dept DepIdList(Integer id);

    //    修改部门
    @Update("update dept set name=#{name} , update_time=#{updateTime} where id=#{id}")
    public void DepUpdate(Dept dept);
}
