package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {


    /**
     * 分类分页管理
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult CategoryPageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);


    /**
     * 启用，禁用
     * @param status
     * @param id
     */
    void StartOrStop(Integer status, Long id);


    /**
     * 修改分类
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> listCategory(Integer type);

    /**
     * 根据id删除
     * @param id
     */
    void DeleteCategory(Long id);
}
