package com.loveorder.service;

import com.loveorder.common.PageResult;
import com.loveorder.dto.OrderDTO;
import com.loveorder.entity.Order;
import com.loveorder.entity.OrderItem;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     * @param userId 用户ID
     * @param orderDTO 下单请求
     * @return 创建的订单
     */
    Order createOrder(Long userId, OrderDTO orderDTO);

    /**
     * 根据ID获取订单详情（含订单项）
     */
    Order getOrderById(Long id);

    /**
     * 根据订单编号获取订单
     */
    Order getOrderByOrderNo(String orderNo);

    /**
     * 分页查询用户的订单列表
     */
    PageResult<Order> getUserOrders(Long userId, int page, int size);

    /**
     * 更新订单状态
     */
    Order updateOrderStatus(Long id, Integer status);

    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 获取订单的订单项列表
     */
    List<OrderItem> getOrderItems(Long orderId);

    /**
     * 支付订单
     */
    Order payOrder(Long userId, Long orderId);
}
