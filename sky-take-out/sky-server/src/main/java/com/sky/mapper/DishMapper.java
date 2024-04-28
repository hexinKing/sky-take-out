package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
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
    Page<DishVO> DishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void AddDish(Dish dish);


    /**
     * 根据菜品id查询菜品
     * @param id
     * @return
     */
    @Select(" select * from dish where id= #{id}")
    Dish ListDish(Long id);


    /**
     * 菜品起售、停售及修改
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void StartOrStop(Dish dish);

    /**
     * 菜品删除
     * @param ids
     */
    void DeleteDish(Long[] ids);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @param status
     * @return
     */
    @Select("select * from dish where category_id=#{categoryId} and status=#{status}")
    List<Dish> catId(Long categoryId, Integer status);

    /**
     * 根据主键查询菜品
     * @param ids
     * @return
     */
    List<Dish> ListDishIds(Long[] ids);
}
