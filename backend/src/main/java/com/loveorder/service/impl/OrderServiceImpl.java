package com.loveorder.service.impl;

import cn.hutool.core.util.IdUtil;
import com.loveorder.common.BusinessException;
import com.loveorder.common.PageResult;
import com.loveorder.common.ResultCode;
import com.loveorder.dto.OrderDTO;
import com.loveorder.entity.MenuItem;
import com.loveorder.entity.Order;
import com.loveorder.entity.OrderItem;
import com.loveorder.entity.User;
import com.loveorder.repository.MenuItemRepository;
import com.loveorder.repository.OrderItemRepository;
import com.loveorder.repository.OrderRepository;
import com.loveorder.repository.UserRepository;
import com.loveorder.service.LovePointService;
import com.loveorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;
    private final LovePointService lovePointService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Long userId, OrderDTO orderDTO) {
        // 验证用户存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        // 生成订单编号
        String orderNo = generateOrderNo();

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStatus(0); // 待支付
        order.setMoodTag(orderDTO.getMoodTag());
        order.setDiningMode(orderDTO.getDiningMode() != null ? orderDTO.getDiningMode() : 0);
        order.setTableNo(orderDTO.getTableNo());
        order.setRemark(orderDTO.getRemark());

        // 计算订单总金额并创建订单项
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderDTO.OrderItemDTO itemDTO : orderDTO.getItems()) {
            // 查询菜品
            MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                    .orElseThrow(() -> new BusinessException(ResultCode.MENU_NOT_FOUND));

            if (menuItem.getStatus() != 1) {
                throw new BusinessException("菜品 [" + menuItem.getName() + "] 已下架");
            }

            // 创建订单项（快照菜品信息）
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setMenuItemName(menuItem.getName());
            orderItem.setMenuItemImage(menuItem.getImage());
            orderItem.setPrice(menuItem.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());

            // 增加菜品销量
            menuItem.setSalesCount(menuItem.getSalesCount() + itemDTO.getQuantity());
            menuItemRepository.save(menuItem);
        }

        order.setTotalAmount(totalAmount);

        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 保存订单项
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(savedOrder.getId());
        }
        orderItemRepository.saveAll(orderItems);

        log.info("订单创建成功: orderNo={}, userId={}, totalAmount={}", orderNo, userId, totalAmount);
        return savedOrder;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ORDER_NOT_FOUND));
    }

    @Override
    public Order getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    public PageResult<Order> getUserOrders(Long userId, int page, int size) {
        Page<Order> pageResult = orderRepository.findByUserIdOrderByCreateTimeDesc(userId, PageRequest.of(page - 1, size));
        return PageResult.of(pageResult);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderStatus(Long id, Integer status) {
        Order order = getOrderById(id);

        // 状态流转校验
        validateStatusTransition(order.getStatus(), status);

        order.setStatus(status);

        // 根据状态设置时间
        if (status == 1) {
            order.setPayTime(LocalDateTime.now());
        } else if (status == 3) {
            order.setCompleteTime(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        Order order = getOrderById(orderId);

        // 验证订单归属
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        // 只有待支付和已支付的订单可以取消
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new BusinessException(ResultCode.ORDER_CANNOT_CANCEL);
        }

        order.setStatus(4); // 已取消
        orderRepository.save(order);
        log.info("订单已取消: orderId={}, orderNo={}", orderId, order.getOrderNo());
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order payOrder(Long userId, Long orderId) {
        Order order = getOrderById(orderId);

        // 验证订单归属
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        // 只有待支付订单可以支付
        if (order.getStatus() != 0) {
            throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }

        // 模拟支付成功，更新订单状态
        order.setStatus(1);
        order.setPayTime(LocalDateTime.now());
        Order paidOrder = orderRepository.save(order);

        // 支付成功后增加爱心积分
        lovePointService.addPointsForOrder(userId);

        log.info("订单支付成功: orderId={}, orderNo={}", orderId, order.getOrderNo());
        return paidOrder;
    }

    /**
     * 生成订单编号
     * 格式：LO + 年月日时分秒 + 6位随机数
     */
    private String generateOrderNo() {
        return "LO" + IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * 校验订单状态流转是否合法
     */
    private void validateStatusTransition(Integer currentStatus, Integer newStatus) {
        // 定义合法的状态流转
        switch (currentStatus) {
            case 0: // 待支付 -> 已支付、已取消
                if (newStatus != 1 && newStatus != 4) {
                    throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
                }
                break;
            case 1: // 已支付 -> 制作中、已取消
                if (newStatus != 2 && newStatus != 4) {
                    throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
                }
                break;
            case 2: // 制作中 -> 已完成
                if (newStatus != 3) {
                    throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
                }
                break;
            case 3: // 已完成 -> 终态
            case 4: // 已取消 -> 终态
                throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
            default:
                throw new BusinessException(ResultCode.ORDER_STATUS_ERROR);
        }
    }
}
