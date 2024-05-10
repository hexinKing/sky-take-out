package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    /**
     * 取消订单
     * @param orders
     * @return
     */
    @Override
    public void cancelOrder(Orders orders) {

    }

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO userSubmit(OrdersSubmitDTO ordersSubmitDTO) {
//        对传过来的数据进行数据拷贝
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
//        判断购物车、地址簿中数据是否为空，
//        通过地址id获取相应的数据并添加到orders中
        AddressBook addressBook = addressBookMapper.GetByID(orders.getAddressBookId());
        if (addressBook==null) {
//            购物车为空抛出异常
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
//        通过当前用户id，获取相应的数据并添加到orders中
        Long userID = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userID);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectShoppingCart(shoppingCart);
        if (shoppingCarts==null || shoppingCarts.size()==0) {
//          地址簿为空抛出异常
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

//        添加一条数据到orders表中
        orders.setUserId(addressBook.getUserId());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        User user = userMapper.getById(userID);
        orders.setUserName(user.getName());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.PENDING_PAYMENT);
//      以当前的时间戳作为订单表的订单号
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orderMapper.InsertOrders(orders);
//        添加n条数据到order_detail表中
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart , orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.InsertOrderDetail(orderDetailList);
//        清除购物车中的数据
        shoppingCartMapper.DeleteShoppingCart(userID);
//        放回VO数据
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderAmount(ordersSubmitDTO.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        return orderSubmitVO;
    }



    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "鑫兴外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }


}
