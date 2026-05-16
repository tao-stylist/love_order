package com.loveorder.service;

import com.loveorder.dto.ReviewDTO;
import com.loveorder.entity.OrderReview;

/**
 * 评价服务接口
 */
public interface ReviewService {

    /**
     * 创建评价
     */
    OrderReview createReview(Long userId, ReviewDTO reviewDTO);

    /**
     * 根据订单ID获取评价
     */
    OrderReview getReviewByOrderId(Long orderId);
}
