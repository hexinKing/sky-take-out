package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper  {


    /**
     * 添加数据
     * @param orders
     */
    void InsertOrders(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 根据订单id查询订单表数据
     * @param id
     * @return
     */
    @Select("Select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 查询所有的订单数据(分页查询)
     * @return
     * @param ordersPageQueryDTO
     */
    Page<OrderVO> getAllOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Select("select count(*) from orders where status=#{status}")
    Integer statistics(Integer status);

}
