package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Api(tags = "分类管理")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 分类管理分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult> CategoryPageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult= categoryService.CategoryPageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增分类")
    public Result addCategory(CategoryDTO categoryDTO){
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }


    /**
     * 启用，禁用
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "启用，禁用")
    public Result StartOrStop(@PathVariable Integer status , Long id){
        categoryService.StartOrStop(status,id);
        return Result.success();
    }


    /**
     * 分类修改
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation(value = "分类修改")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }


    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据类型查询分类")
    public Result<List<Category>> listCategory(Integer type){
        List<Category> list = categoryService.listCategory(type);
        return Result.success(list);
    }


    /**
     * 根据id删除
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation(value = "根据id删除")
    public Result DeleteCategory(Long id){
        categoryService.DeleteCategory(id);
        return Result.success();
    }




}
