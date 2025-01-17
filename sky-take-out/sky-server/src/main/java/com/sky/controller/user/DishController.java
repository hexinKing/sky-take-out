package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user/dish")
@RestController("userDishController")
@Api(tags = "C端-菜品浏览接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据分类id查询以及菜品口味
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    @Cacheable(cacheNames = "userCategoryIdDish" , key = "#categoryId")//缓存菜品及其口味信息
    public Result<List<DishVO>> getCategoryId(Long categoryId){
        List<DishVO> dishes = dishService.ListDish(categoryId);
        log.info("根据分类id查询菜品:{}",dishes);
        return Result.success(dishes);
    }

}
