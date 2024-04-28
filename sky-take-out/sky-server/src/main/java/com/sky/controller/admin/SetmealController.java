package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "userCategoryIdSetmeal" , key = "#setmealDTO.categoryId")
    public Result AddSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.AddSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> PageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult= setmealService.PageSetmeal(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    @CacheEvict(cacheNames = "userCategoryIdSetmeal" , allEntries = true)
    public Result StartOrStop(@PathVariable Integer status ,Long id){
        setmealService.StartOrStop(status,id);
        return Result.success();
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "userCategoryIdSetmeal" , allEntries = true)
    public Result UpdateSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.UpdateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
//    因为删除套餐必须在停售的状态下，而用户端只展示起售的套餐，同时起售停售回清除Redis中的缓存
//    @CacheEvict(cacheNames = "userCategoryIdSetmeal" , allEntries = true)
    public Result DeleteSetmeal(@RequestParam List<Long> ids){
        setmealService.DeleteSetmeal(ids);
        return Result.success();
    }



}
