package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;


    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
        @GetMapping("/list")
        @ApiOperation("根据分类id查询套餐")
        @Cacheable(cacheNames = "userCategoryIdSetmeal" , key = "#categoryId")
        public Result<List<SetmealVO>> getCategoryId(Long categoryId){
            List<SetmealVO> setmealVOS = setmealService.ListSetmeal(categoryId);
            return Result.success(setmealVOS);
    }


    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    @Cacheable(cacheNames = "userSetmealIdDish" , key = "#id")
    public Result<List<DishItemVO>> getDishId(@PathVariable("id") Long id){
        List<DishItemVO> dishItemVO = setmealService.getSetmealId(id);
        return Result.success(dishItemVO);
    }
}
