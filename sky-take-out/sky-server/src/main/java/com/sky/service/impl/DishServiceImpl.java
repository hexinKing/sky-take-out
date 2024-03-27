package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.mapper.Dish_flavorMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl  implements DishService {

    @Autowired
    private Dish_flavorMapper dish_flavorMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult DishPageQuery(DishPageQueryDTO dishPageQueryDTO) {

//        设置分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
//        执行分页查询获取数据
        Page<Dish> page =dishMapper.DishPageQuery(dishPageQueryDTO);
//        获取总记录数和当前页数据集合
        long total = page.getTotal();
        List<Dish> result = page.getResult();
        log.info("总记录数：{}",total);
        log.info("当前页数据集合：{}",result);
//        对数据进行封装
        PageResult pageResult = new PageResult(total, result);

        return pageResult;
    }


    /**
     * 新增菜品
     * @param dishVO
     */
    @Override
    public void AddDish(DishVO dishVO) {
//        对DishVO进行数据拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishVO,dish);
        DishFlavor dishFlavor = dishVO.getFlavors().get(0);

//        新增菜品默认为停售状态，需要手动进行启售
        dish.setStatus(StatusConstant.DISABLE);
//        新增菜品表数据
        dishMapper.AddDish(dish);
//        新增菜品口味数据
        dish_flavorMapper.AddDish_flavor(dishFlavor);

    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish = new Dish();
        dish.setId(id);
//        查询菜品信息
        List<Dish> dishes = dishMapper.ListDish(dish);
//       查询菜品口味信息
       DishFlavor dishFlavor= dish_flavorMapper.getById(id);
//       对数据进行封装
        DishVO dishVO = new DishVO();
        dish=dishes.get(0);
        BeanUtils.copyProperties(dish,dishVO);

        List<DishFlavor> flavors = new ArrayList<>();
        flavors.add(dishFlavor);

        dishVO.setFlavors(flavors);

        return dishVO;
    }

    /**
     * 根据分类id查询
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> ListDish(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        List<Dish> dishes = dishMapper.ListDish(dish);
        log.info("根据分类id查询数据为：{}",dishes);
        return dishes;
    }

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     */
    @Override
    public void StartOrStop(Integer status, Long id) {
//        对数据进行封装
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.StartOrStop(dish);
    }

    /**
     * 菜品删除
     * @param ids
     * @return
     */
    @Override
    public void DeleteDish(Long[] ids) {
        dishMapper.DeleteDish(ids);
    }

    /**
     * 修改菜品
     * @param dishVO
     * @return
     */
    @Override
    public void UpdateDish(DishVO dishVO) {
//        修改菜品表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishVO,dish);
        dishMapper.StartOrStop(dish);
//        修改菜品口味表
        DishFlavor dishFlavor ;
        dishFlavor=dishVO.getFlavors().get(0);
        dish_flavorMapper.UpdateDish(dishFlavor);
    }


}
