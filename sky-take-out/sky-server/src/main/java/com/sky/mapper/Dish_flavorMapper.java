package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface Dish_flavorMapper {


    /**
     * 新增菜品口味
     * @param dishFlavor
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("INSERT INTO dish_flavor(dish_id, name, value) values (#{dishId},#{name},#{value})")
    void AddDish_flavor(DishFlavor dishFlavor);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{id}")
    DishFlavor getById(Long id);

    /**
     * 修改菜品口味
     * @param dishFlavor
     */
    void UpdateDish(DishFlavor dishFlavor);
}
