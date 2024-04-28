package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> listShoppingCart();

    /**
     * 清空购物车
     * @return
     */
    void DeleteShoppingCart();

    /**
     * 删除购物车中一个商品
     * @return
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}