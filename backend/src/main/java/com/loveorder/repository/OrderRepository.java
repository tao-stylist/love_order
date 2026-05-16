package com.loveorder.repository;

import com.loveorder.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 订单 Repository
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据订单编号查找
     */
    Order findByOrderNo(String orderNo);

    /**
     * 根据用户ID分页查找订单
     */
    Page<Order> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    /**
     * 根据用户ID和状态查找订单
     */
    List<Order> findByUserIdAndStatus(Long userId, Integer status);

    /**
     * 根据情侣ID分页查找订单
     */
    Page<Order> findByCoupleIdOrderByCreateTimeDesc(Long coupleId, Pageable pageable);

    /**
     * 根据用户ID查找所有订单
     */
    List<Order> findByUserId(Long userId);
}
