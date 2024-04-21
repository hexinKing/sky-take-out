package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl  implements DishService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

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
        Page<DishVO> page =dishMapper.DishPageQuery(dishPageQueryDTO);
//        获取总记录数和当前页数据集合
        long total = page.getTotal();
        List<DishVO> result = page.getResult();
        log.info("总记录数：{}",total);
        log.info("当前页数据集合：{}",result);
//        对数据进行封装
        PageResult pageResult = new PageResult(total, result);

        return pageResult;
    }


    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    public void AddDish(DishDTO dishDTO) {
//        对DishVO进行数据拷贝
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

//        新增菜品默认为停售状态，需要手动进行启售
        dish.setStatus(StatusConstant.DISABLE);
//        新增菜品表数据
        dishMapper.AddDish(dish);
//        先判断口味是否需要添加
        if (dishDTO.getFlavors()!=null) {
//        设置dish_flavor表的dish_id
            Long dish_id=dish.getId();
            List<DishFlavor> flavors = dishDTO.getFlavors();
//        循环为口味集合赋菜品id
            flavors.forEach(flavor -> flavor.setDishId(dish_id));
            log.info("菜品口味集合数据：{}",flavors);
//        新增菜品口味数据
            dishFlavorMapper.AddDish_flavor(flavors);
        }

    }

    /**
     * 根据菜品id查询菜品及口味
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
//        查询菜品信息
        Dish dish= dishMapper.ListDish(id);
//       查询菜品口味信息
        List<DishFlavor> dishFlavor= dishFlavorMapper.getById(id);
//       对数据进行封装
        DishVO dishVO = new DishVO();
        log.info("根据id查询菜品信息:{}",dish);
//       对菜品表进行拷贝
        BeanUtils.copyProperties(dish,dishVO);
        log.info("根据id查询菜品口味信息:{}",dishFlavor);
        dishVO.setFlavors(dishFlavor);

        return dishVO;
    }

    /**
     * 根据分类id查询菜品以及菜品口味
     * @param categoryId
     * @return
     */
    @Override
    public List<DishVO> ListDish(Long categoryId) {
//       对数据进行封装
        List<DishVO> dishVOS ;
//        查询菜品信息
        List<Dish> dishes = dishMapper.catId(categoryId);
        log.info("根据分类id查询菜品信息:{}",dishes);
//       对菜品表进行拷贝(fastjson 序列化反序列化方法（深拷贝）)
        dishVOS= JSONObject.parseArray(JSONObject.toJSONString(dishes), DishVO.class);
//       查询菜品口味信息
        for (DishVO dishVO : dishVOS) {
            List<DishFlavor> dishFlavors = dishFlavorMapper.getById(dishVO.getId());
            log.info("根据菜品id查询菜品口味信息:{}",dishFlavors);
            dishVO.setFlavors(dishFlavors);
        }
        log.info("根菜品及口味信息:{}",dishVOS);

        return dishVOS;
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
     * 菜品删除（批量删除）
     * @param ids
     * @return
     */
    @Override
    public void DeleteDish(Long[] ids) {

//        判断是否可以删除,起售中的菜品不能删除
        List<Dish> dishList = dishMapper.ListDishIds(ids);
        for (Dish dish : dishList) {
            if (dish.getStatus() == StatusConstant.ENABLE) {//起售中不能删除
//                抛出异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
//        判断是否可以删除,被套餐关联的菜品不能删除
        List<SetmealDish> setmealDishList = setmealDishMapper.ListSetmealDish(ids);
        for (SetmealDish setmealDish : setmealDishList) {
            if (setmealDish!=null ) {//套餐关联了菜品不能删除
//                抛出异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
//        删除菜品数据
        dishMapper.DeleteDish(ids);
//        删除菜品后，关联的口味数据有需要删除掉
        dishFlavorMapper.DeleteDishFlavor(ids);


    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @Override
    public void UpdateDish(DishDTO dishDTO) {
//        修改菜品表
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.StartOrStop(dish);

//        交互上是修改口味，背后逻辑上是先添加在删除
//        1.删除口味数据
        Long[] dishId = new Long[]{dishDTO.getId()};
        dishFlavorMapper.DeleteDishFlavor(dishId);
//        2.添加对应的口味数据
//        判断是否需要添加口味
        if (dishDTO.getFlavors()!=null && dishDTO.getFlavors().size()>0) {
            List<DishFlavor> flavors = dishDTO.getFlavors();
//            向flavors插入dish_id数据
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDTO.getId());
            }
            dishFlavorMapper.AddDish_flavor(flavors);
        }

    }


    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Override
    public List<SetmealVO> ListSetmeal(Long categoryId) {
        List<SetmealVO> setmealVOS = dishMapper.ListSetmeal(categoryId);
        return setmealVOS;
    }


}
