package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "菜品分页管理")
    public Result<PageResult>  DishPageQuery( DishPageQueryDTO dishPageQueryDTO){
         PageResult pageResult = dishService.DishPageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    public Result AddDish(@RequestBody DishDTO dishDTO){
        dishService.AddDish(dishDTO);
        return Result.success();
    }


    /**
     * 根据菜品id查询菜品及口味
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询")
    public Result<DishVO> getById(@PathVariable Long id){
        DishVO dishVO= dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 根据分类id查询菜品
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<DishVO>> ListDish(Long categoryId){
        log.info("菜品表的分类id为：{}",categoryId);
        List<DishVO> dish= dishService.ListDish(categoryId);
        return Result.success(dish);
    }

    /**
     * 菜品起售、停售
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "菜品起售、停售")
    public Result StartOrStop(@PathVariable Integer status ,Long id){
        dishService.StartOrStop(status,id);
        return Result.success();
    }


    /**
     * 菜品删除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "菜品删除")
    public Result DeleteDish(Long[] ids){
        dishService.DeleteDish(ids);
        return Result.success();
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改菜品")
    public Result UpdateDish(@RequestBody DishDTO dishDTO){
        dishService.UpdateDish(dishDTO);
        return Result.success();
    }


}
