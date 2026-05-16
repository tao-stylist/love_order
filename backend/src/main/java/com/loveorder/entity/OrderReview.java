package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单评价实体类
 */
@Data
@Entity
@Table(name = "order_review")
public class OrderReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 订单ID */
    @Column(nullable = false, unique = true)
    private Long orderId;

    /** 用户ID */
    @Column(nullable = false)
    private Long userId;

    /** 评分：1-5 */
    @Column(nullable = false)
    private Integer rating;

    /** 评价内容 */
    @Column(length = 1000)
    private String content;

    /** 评价图片（逗号分隔URL） */
    @Column(length = 2000)
    private String images;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}
