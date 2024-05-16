package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量添加
     * @param orderDetailList
     */
    void InsertOrderDetail(List<OrderDetail> orderDetailList);

    /**
     * 根据订单id查询订单明细表数据
     * @param id
     * @return
     */
    @Select("Select * from order_detail where order_id=#{id}")
    List<OrderDetail> getById(Long id);
}
