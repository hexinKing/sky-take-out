package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 删除分类的特殊处理
     * @param categoryId
     * @return
     */
    @Select("select count(*) from dish where category_id=#{categoryId}")
    Integer categoryId(Long categoryId);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<Dish> DishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void AddDish(Dish dish);


    /**
     * 根据分类id查询,根据id查询
     * @param dish
     * @return
     */
    List<Dish> ListDish(Dish dish);


    /**
     * 菜品起售、停售
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void StartOrStop(Dish dish);

    /**
     * 菜品删除
     * @param ids
     */
    void DeleteDish(Long[] ids);

}
