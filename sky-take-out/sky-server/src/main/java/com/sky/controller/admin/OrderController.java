package com.sky.controller.admin;

import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/order")
@RestController("adminOrderController")
@Api(tags = "订单管理接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 取消订单
     * @param orders
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancelOrder(@RequestBody Orders orders){
        orderService.cancelOrder(orders);
        return Result.success();
    }

}
