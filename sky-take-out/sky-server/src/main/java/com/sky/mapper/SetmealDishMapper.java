package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询所有数据
     * @param ids
     * @return
     */
    List<SetmealDish> ListSetmealDish(Long[] ids);
}
