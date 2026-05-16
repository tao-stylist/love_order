package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 每日推荐实体类
 * 每日为情侣推荐菜品
 */
@Data
@Entity
@Table(name = "daily_recommend")
public class DailyRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 菜品ID */
    @Column(nullable = false)
    private Long menuItemId;

    /** 推荐日期 */
    @Column(nullable = false, length = 10)
    private String recommendDate;

    /** 推荐理由 */
    @Column(length = 200)
    private String reason;

    /** 推荐顺序 */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.sortOrder == null) {
            this.sortOrder = 0;
        }
    }
}
