package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    /**
     * 新增菜品口味
     * @param dishFlavor
     */
    void AddDish_flavor(List<DishFlavor> dishFlavor);

    /**
     * 根据菜品id查询菜品口味
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> getById(Long id);

    /**
     * 修改菜品口味
     * @param dishFlavor
     */
    void UpdateDish(DishFlavor dishFlavor);

    /**
     * 根据菜品ID删除菜品口味
     * @param ids
     */
    void DeleteDishFlavor(Long[] ids);
}
