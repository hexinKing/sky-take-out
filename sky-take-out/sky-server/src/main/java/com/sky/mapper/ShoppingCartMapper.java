package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 用户端添加购物车
     * @param shoppingCart
     */
    void addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 查询购物车数据
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> selectShoppingCart(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @return
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void DeleteShoppingCart(Long userId);

    /**
     * 修改商品数据
     * @param shoppingCart
     */
    @Update("update shopping_cart set number=#{number} where user_id=#{userId} and dish_id=#{dishId}")
    void updateShoppingCart(ShoppingCart shoppingCart);

    /**
     * 删除购物车中的一个商品
     * @param shoppingCart
     */
    void subShoppingCart(ShoppingCart shoppingCart);
}
