package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static  final Integer number=1;
    private static  final Integer zero=0;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
//        通过微信用户的唯一标识获取用户id
        Long userId = BaseContext.getCurrentId();
        LocalDateTime currentTime = LocalDateTime.now();
        shoppingCart = shoppingCart.builder()
                .userId(userId)
                .dishId(shoppingCartDTO.getDishId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .setmealId(shoppingCartDTO.getSetmealId())
                .number(number)
                .createTime(currentTime)
                .build();

//        根据shoppingCartDTO判断该用户的shopping_cart表中是否有该条购物车数据
        List<ShoppingCart> sc = shoppingCartMapper.selectShoppingCart(shoppingCart);
        if (sc!=null && sc.size()>0){
//        有则number商品数量加一
            shoppingCart.setNumber(sc.get(zero).getNumber()+1);
            log.info("修改的商品数据为：{}",shoppingCart);
//           根据用户id和商品id修改购物车中的商品数量,退出
            shoppingCartMapper.updateShoppingCart(shoppingCart);
        }else {
//        无则将其添加到shopping_cart数据库中
//            分别判断是菜品数据还是套餐数据
            if (shoppingCartDTO.getDishId()!=null) {
//                菜品数据
//        利用菜品id获取商品名称、商品图片路径、商品单价
                Dish dish = dishMapper.ListDish(shoppingCartDTO.getDishId());
//        封装数据并添加
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else {
//                套餐数据
//        利用套餐id获取商品名称、商品图片路径、商品单价
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
//        封装数据并添加
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCartMapper.addShoppingCart(shoppingCart);
        }

    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> listShoppingCart() {
//        获取用户id，根据用户id查询购物车信息
        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        return shoppingCartMapper.selectShoppingCart(shoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @Override
    public void DeleteShoppingCart() {
//        获取用户id，根据用户id清空购物车信息
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.DeleteShoppingCart(userId);
    }

    /**
     * 删除购物车中一个商品
     * @return
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
//        获取用户id
        Long userId = BaseContext.getCurrentId();
        shoppingCart=shoppingCart.builder()
                .userId(userId)
                .dishId(shoppingCartDTO.getDishId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .build();
//        判断要删除的商品的数量是否大于1
        List<ShoppingCart> sc = shoppingCartMapper.selectShoppingCart(shoppingCart);
        if (sc.get(zero).getNumber()>1) {
//        是则修商品数量-1
            shoppingCart.setNumber(sc.get(zero).getNumber()-1);
            shoppingCartMapper.updateShoppingCart(shoppingCart);
        }else {
//        否则删除商品
            log.info("删除的商品为：{}",shoppingCart);
            shoppingCartMapper.subShoppingCart(shoppingCart);
        }

    }
}
