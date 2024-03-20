package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {

    /**
     * 删除分类的特殊处理
     * @param categoryId
     * @return
     */
    @Select("select count(*) from setmeal where category_id=#{categoryId}")
    Integer categoryId(Long categoryId);
}
