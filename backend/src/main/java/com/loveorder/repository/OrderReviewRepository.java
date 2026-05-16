package com.loveorder.repository;

import com.loveorder.entity.OrderReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 订单评价 Repository
 */
public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {

    /**
     * 根据订单ID查找评价
     */
    Optional<OrderReview> findByOrderId(Long orderId);

    /**
     * 根据用户ID分页查找评价
     */
    Page<OrderReview> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    /**
     * 判断订单是否已评价
     */
    boolean existsByOrderId(Long orderId);
}
