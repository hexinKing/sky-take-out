package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheEvict;

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
    @CacheEvict(cacheNames = "userSetmealIdDish" , allEntries = true)
    void AddsetmealDish(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐口味表的所有数据
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getById(Long id);

    /**
     * 修改套餐菜品数据
     * @param setmealDishes
     * @return
     */
    @CacheEvict(cacheNames = "userSetmealIdDish" , allEntries = true)
    void UpdateSetmealDish(SetmealDish setmealDishes);

    /**
     * 通过套餐id和菜品id套餐菜品信息
     * @return
     * @param shoppingCartDTO
     */
    SetmealDish getDishIdSetmealId(ShoppingCartDTO shoppingCartDTO);
}
