package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 分类分页管理
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult CategoryPageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
//        设置分页查询
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
//        执行分页查询获取数据
        Page<Category>page =categoryMapper.CategoryPageQuery(categoryPageQueryDTO);
//        获取总记录数和当前页数据集合
        long total = page.getTotal();
        List<Category> result = page.getResult();
        log.info("总记录数：{}",total);
        log.info("当前页数据集合：{}",result);
//        对数据进行封装
        PageResult pageResult = new PageResult(total, result);
        return pageResult;


    }


    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
//        进行属性对象拷贝
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.addCategory(category);
    }


    /**
     * 启用，禁用
     * @param status
     * @param id
     */
    @Override
    public void StartOrStop(Integer status, Long id) {
        Category category = new Category();
//        对数据进行封装
        category.setStatus(status);
        category.setId(id);
        categoryMapper.StartOrStop(category);
    }


    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
//        进行属性对象拷贝
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.StartOrStop(category);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    public List<Category> listCategory(Integer type) {
        Category category = new Category();
        category.setType(type);
        List<Category> List = categoryMapper.listCategory(category);
        return List;
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    @Transactional
    public void DeleteCategory(Long id) {
//        需要判断该分类下是否还有产品， “分类下有产品不可删除”；
//        1.判断该id对应的分类类型
        Category category = new Category();
        category.setId(id);

        List<Category> list = categoryMapper.listCategory(category);
        log.info("分类的id为：{}",id);
        log.info("分类的Type为：{}",list.get(0).getType());
        if (list.get(0).getType()==1){//菜品分类
//            调用菜品模块的方法来判断该分类下是否含有产品
            Integer num = dishMapper.categoryId(id);
            if (num>0){//该分类下含有产品
//                返回异常，不做删除操作
               throw  new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
            }
        }else {//套餐分类
            Integer num = setmealMapper.categoryId(id);
            if (num>0){//该分类下含有产品
//                返回异常，不做删除操作
                throw  new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
            }
        }
//        进行删除操作
        categoryMapper.deleteCategory(id);
    }


}
