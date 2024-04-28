package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpI implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void AddSetmeal(SetmealDTO setmealDTO) {
//        对setmealDTO进行数据拷贝
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
//        新增套餐数据
//        新增套餐默认为停售状态，需要手动进行启售
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealMapper.AddSetmeal(setmeal);
//        新增套餐菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.AddsetmealDish(setmealDishes);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult PageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        //        设置分页查询
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        //        执行分页查询获取数据
        Page<SetmealVO> page =setmealMapper.PageSetmeal(setmealPageQueryDTO);
//        返回数据
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
//        查询套餐数据
        Setmeal setmeal = setmealMapper.getById(id);
//        查询套餐菜品数据
        List<SetmealDish> setmealDishes = setmealDishMapper.getById(id);
//        对数据进行封装
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     * @return
     */
    @Override
    public void StartOrStop(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
//        起售
        if (status== StatusConstant.ENABLE) {
            //        套餐内如果有停售菜品，则套餐无法上架
//        查询套餐菜品表查询所有数据，用菜品ID查看该菜品是否停售
            List<SetmealDish> setmealDishList = setmealDishMapper.getById(id);
            for (SetmealDish setmealDish : setmealDishList) {
                Dish dish = dishMapper.ListDish(setmealDish.getDishId());
                if (dish.getStatus()== StatusConstant.DISABLE) {
//                表示该菜品为停售状态
//                抛出异常，套餐内包含未启售菜品，无法启售
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
//        停售
        setmealMapper.StartOrStop(setmeal);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @Override
    @Transactional
    public void UpdateSetmeal(SetmealDTO setmealDTO) {
//        修改套餐数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        log.info("套餐数据:{}",setmeal);

//        起售
        if (setmealDTO.getStatus()== StatusConstant.ENABLE) {
//        套餐内如果有停售菜品，则套餐无法上架
//        查询套餐菜品表查询所有数据，用套餐id查看该菜品是否停售
            List<SetmealDish> setmealDishList = setmealDishMapper.getById(setmealDTO.getId());
            for (SetmealDish setmealDish : setmealDishList) {
                Dish dish = dishMapper.ListDish(setmealDish.getDishId());
                if (dish.getStatus()== StatusConstant.DISABLE) {
//                表示该菜品为停售状态
//                抛出异常，套餐内包含未启售菜品，无法启售
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }

        setmealMapper.StartOrStop(setmeal);
        if (setmealDTO.getSetmealDishes()!=null && setmealDTO.getSetmealDishes().size()>0) {
            //        修改套餐菜品数据
            for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
                log.info("套餐菜品数据:{}",setmealDish);
                setmealDishMapper.UpdateSetmealDish(setmealDish);
            }

        }

    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @Override
    public void DeleteSetmeal(List<Long> ids) {
//        在售状态下，不可删除套餐
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus()==StatusConstant.ENABLE){
//                抛出异常，起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
//        删除套餐
        setmealMapper.DeleteSetmeal(ids);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id
     * @return
     */
    @Override
    public List<DishItemVO> getSetmealId(Long id) {
        List<SetmealDish> setmealDishes = setmealDishMapper.getById(id);
        //        对数据进行封装，序列化反序列化方法
        List<DishItemVO> dishItemVOS = JSONObject.parseArray(JSONObject.toJSONString(setmealDishes), DishItemVO.class);
        log.info("根据套餐id查询包含的菜品数据：{}",dishItemVOS);
        return dishItemVOS;
    }


    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Override
    public List<SetmealVO> ListSetmeal(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
//        只查询起售状态下的套餐
        setmeal.setStatus(StatusConstant.ENABLE);
        List<SetmealVO> setmealVOS = setmealMapper.ListSetmeal(setmeal);
        return setmealVOS;
    }


}
