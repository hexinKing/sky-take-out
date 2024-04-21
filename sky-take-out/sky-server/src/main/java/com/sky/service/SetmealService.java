package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void AddSetmeal(SetmealDTO setmealDTO);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult PageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getById(Long id);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     * @return
     */
    void StartOrStop(Integer status, Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    void UpdateSetmeal(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    void DeleteSetmeal(List<Long> ids);

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    List<DishItemVO> getSetmealId(Long id);
}
