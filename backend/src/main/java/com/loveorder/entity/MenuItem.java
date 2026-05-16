package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品实体类
 */
@Data
@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 菜品名称 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 菜品描述 */
    @Column(length = 500)
    private String description;

    /** 菜品图片URL */
    @Column(length = 500)
    private String image;

    /** 价格 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** 分类ID */
    @Column(nullable = false)
    private Long categoryId;

    /** 心情标签（逗号分隔，如：浪漫,甜蜜,惊喜） */
    @Column(length = 200)
    private String moodTags;

    /** 推荐指数：1-5 */
    @Column(columnDefinition = "TINYINT DEFAULT 3")
    private Integer recommendScore;

    /** 销量 */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer salesCount;

    /** 状态：0-下架，1-上架 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Integer status;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        if (this.salesCount == null) {
            this.salesCount = 0;
        }
        if (this.recommendScore == null) {
            this.recommendScore = 3;
        }
        if (this.status == null) {
            this.status = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
