package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 菜品分类实体类
 */
@Data
@Entity
@Table(name = "menu_category")
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 分类名称 */
    @Column(nullable = false, length = 50)
    private String name;

    /** 分类图标URL */
    @Column(length = 500)
    private String icon;

    /** 排序号 */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer sortOrder;

    /** 状态：0-禁用，1-启用 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Integer status;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.sortOrder == null) {
            this.sortOrder = 0;
        }
        if (this.status == null) {
            this.status = 1;
        }
    }
}
