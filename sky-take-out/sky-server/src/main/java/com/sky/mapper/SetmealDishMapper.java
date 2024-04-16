package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询所有数据
     * @param ids
     * @return
     */
    List<SetmealDish> ListSetmealDish(Long[] ids);

    /**
     * 新增套餐菜品数据
     * @param setmealDishes
     */
    void AddsetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询所有数据
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getById(Long id);

    /**
     * 修改套餐菜品
     * @param setmealDishes
     * @return
     */
    void UpdateSetmealDish(SetmealDish setmealDishes);
}
