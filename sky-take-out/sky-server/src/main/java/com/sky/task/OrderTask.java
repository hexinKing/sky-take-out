package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 自定义定时任务类
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 定时任务，校验用户端订单超时未支付，自动取消
     */
    @Scheduled(cron = "0 0/5 * * * ? ")//Cron表达式触发任务,每五分钟执行一次
    public void processTimeoutOrder(){
        log.info("检查订单超时定时任务开始：{}", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(15);
        Integer status = Orders.PENDING_PAYMENT;
//        判断当前时间减15分钟是否大于用户下单时间且订单状态为1
        List<Orders> ordersList =  orderMapper.getStatusTime(status , localDateTime);
//        是则为超时将该订单的订单状态修改为已取消，并添加取消原因和取消时间
        if (ordersList!=null && ordersList.size()>0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单未支付已超时");
                orderMapper.update(orders);
            }
        }

    }

    /**
     * 处理一直处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")//每日的凌晨一点触发
    public void processDeliveryOrder(){
        log.info("检查一直处于派送中订单定时任务开始：{}", LocalDateTime.now());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(60);
//        查询所有订单状态在派送中的订单
        List<Orders> ordersList =  orderMapper.getStatusTime(Orders.DELIVERY_IN_PROGRESS , localDateTime);
//        对该订单的订单状态进行修改，改为已完成
        if (ordersList!=null && ordersList.size()>0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }




}
