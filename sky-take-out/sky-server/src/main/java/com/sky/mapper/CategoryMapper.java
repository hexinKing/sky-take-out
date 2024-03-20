package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 分类分页管理
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> CategoryPageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param category
     */
    void addCategory(Category category);


    /**
     * 启用，禁用
     * @param category
     */
    void StartOrStop(Category category);


    /**
     * 根据类型查询分类，根据id查询
     * @param category
     * @return
     */
    List<Category> listCategory(Category category);


    /**
     * 分类删除
     * @param id
     */
    @Delete("delete from category where id=#{id}")
    void deleteCategory(Long id);
}
