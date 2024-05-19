package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 删除分类的特殊处理
     * @param categoryId
     * @return
     */
    @Select("select count(*) from setmeal where category_id=#{categoryId}")
    Integer categoryId(Long categoryId);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void AddSetmeal(Setmeal setmeal);

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> PageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id主键查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    /**
     * 套餐起售、停售及修改
     * @param setmeal
     * @return
     */
    @AutoFill(value = OperationType.UPDATE)
    void StartOrStop(Setmeal setmeal);

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    void DeleteSetmeal(List<Long> ids);

    /**
     * 根据分类id查询套餐
     * @param setmeal
     * @return
     */
    @Select("Select * from setmeal where category_id=#{categoryId} and status=#{status}")
    List<SetmealVO> ListSetmeal(Setmeal setmeal);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
