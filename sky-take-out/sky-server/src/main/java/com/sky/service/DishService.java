package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult DishPageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 新增菜品
     * @param dishDTO
     */
    void AddDish(DishDTO dishDTO);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 根据分类id查询
     * @param categoryId
     * @return
     */
    List<Dish> ListDish(Long categoryId);

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, Long id);

    /**
     * 菜品删除
     * @param ids
     * @return
     */
    void DeleteDish(Long[] ids);

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    void UpdateDish(DishDTO dishDTO);
}
