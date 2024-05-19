package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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

    /**
     * 判断当前时间减15分钟是否大于用户下单时间且订单状态为1,返回查询结果
     * @param status
     * @param localDateTime
     * @return
     */
    @Select("select * from orders where status=#{status} and order_time<#{localDateTime}")
    List<Orders> getStatusTime(Integer status, LocalDateTime localDateTime);

    /**
     * 获取每日已完成的销售额
     * @param hashMap
     * @return
     */
    Double beginAndEnd(HashMap hashMap);

    /**
     * 获取每日订单数和有效订单数
     * @param hashMap
     * @return
     */
    Integer ordersCount(HashMap hashMap);

    /**
     * 获取每日销售的商品名称和销量数量
     * @param hashMap
     * @return
     */
    List<GoodsSalesDTO> getSelectTop10(HashMap hashMap);
}
