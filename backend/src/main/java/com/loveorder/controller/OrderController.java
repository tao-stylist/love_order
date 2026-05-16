package com.loveorder.controller;

import com.loveorder.common.PageResult;
import com.loveorder.common.Result;
import com.loveorder.dto.OrderDTO;
import com.loveorder.entity.Order;
import com.loveorder.entity.OrderItem;
import com.loveorder.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * 处理下单、查询订单、更新状态等
 */
@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @Operation(summary = "创建订单")
    @PostMapping
    public Result<Order> createOrder(Authentication authentication,
                                     @Valid @RequestBody OrderDTO orderDTO) {
        Long userId = (Long) authentication.getPrincipal();
        Order order = orderService.createOrder(userId, orderDTO);
        return Result.success(order);
    }

    /**
     * 获取订单详情（含订单项）
     */
    @Operation(summary = "获取订单详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        List<OrderItem> items = orderService.getOrderItems(id);

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return Result.success(result);
    }

    /**
     * 获取当前用户的订单列表
     */
    @Operation(summary = "获取我的订单列表")
    @GetMapping("/my")
    public Result<PageResult<Order>> getMyOrders(Authentication authentication,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.getUserOrders(userId, page, size));
    }

    /**
     * 支付订单
     */
    @Operation(summary = "支付订单")
    @PostMapping("/{id}/pay")
    public Result<Order> payOrder(Authentication authentication,
                                  @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.payOrder(userId, id));
    }

    /**
     * 取消订单
     */
    @Operation(summary = "取消订单")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancelOrder(Authentication authentication,
                                    @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        orderService.cancelOrder(userId, id);
        return Result.success();
    }

    /**
     * 更新订单状态（管理员使用）
     */
    @Operation(summary = "更新订单状态")
    @PutMapping("/{id}/status")
    public Result<Order> updateOrderStatus(@PathVariable Long id,
                                           @RequestParam Integer status) {
        return Result.success(orderService.updateOrderStatus(id, status));
    }
}
