package com.loveorder.service.impl;

import com.loveorder.common.BusinessException;
import com.loveorder.common.ResultCode;
import com.loveorder.dto.ReviewDTO;
import com.loveorder.entity.Order;
import com.loveorder.entity.OrderReview;
import com.loveorder.repository.OrderRepository;
import com.loveorder.repository.OrderReviewRepository;
import com.loveorder.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 评价服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final OrderReviewRepository orderReviewRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderReview createReview(Long userId, ReviewDTO reviewDTO) {
        // 验证订单存在
        Order order = orderRepository.findById(reviewDTO.getOrderId())
                .orElseThrow(() -> new BusinessException(ResultCode.ORDER_NOT_FOUND));

        // 验证订单归属
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        // 验证订单状态为已完成
        if (order.getStatus() != 3) {
            throw new BusinessException("订单未完成，无法评价");
        }

        // 检查是否已评价
        if (orderReviewRepository.existsByOrderId(reviewDTO.getOrderId())) {
            throw new BusinessException(ResultCode.REVIEW_ALREADY_EXISTS);
        }

        // 创建评价
        OrderReview review = new OrderReview();
        review.setOrderId(reviewDTO.getOrderId());
        review.setUserId(userId);
        review.setRating(reviewDTO.getRating());
        review.setContent(reviewDTO.getContent());
        review.setImages(reviewDTO.getImages());

        OrderReview saved = orderReviewRepository.save(review);
        log.info("评价创建成功: orderId={}, userId={}, rating={}", reviewDTO.getOrderId(), userId, reviewDTO.getRating());
        return saved;
    }

    @Override
    public OrderReview getReviewByOrderId(Long orderId) {
        return orderReviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(ResultCode.REVIEW_NOT_FOUND));
    }
}
