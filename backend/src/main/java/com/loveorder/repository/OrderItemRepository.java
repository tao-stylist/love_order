package com.loveorder.repository;

import com.loveorder.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单项 Repository
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 根据订单ID查找所有订单项
     */
    List<OrderItem> findByOrderId(Long orderId);
}
