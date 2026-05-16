package com.loveorder.controller;

import com.loveorder.common.Result;
import com.loveorder.dto.ReviewDTO;
import com.loveorder.entity.OrderReview;
import com.loveorder.service.LovePointService;
import com.loveorder.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 评价控制器
 * 处理订单评价
 */
@Tag(name = "评价管理", description = "订单评价相关接口")
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final LovePointService lovePointService;

    @Operation(summary = "提交评价")
    @PostMapping
    public Result<OrderReview> createReview(Authentication authentication,
                                            @Valid @RequestBody ReviewDTO reviewDTO) {
        Long userId = (Long) authentication.getPrincipal();
        OrderReview review = reviewService.createReview(userId, reviewDTO);

        // 评价成功后增加爱心积分
        lovePointService.addPointsForReview(userId);

        return Result.success("评价成功，积分+5", review);
    }

    @Operation(summary = "获取订单评价")
    @GetMapping("/order/{orderId}")
    public Result<OrderReview> getReviewByOrderId(@PathVariable Long orderId) {
        return Result.success(reviewService.getReviewByOrderId(orderId));
    }
}
