package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;

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
     * 根据菜品id查询菜品及口味
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 根据分类id查询以及菜品口味
     * @param categoryId
     * @return
     */
    List<DishVO> ListDish(Long categoryId);

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

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    List<SetmealVO> ListSetmeal(Long categoryId);
}
