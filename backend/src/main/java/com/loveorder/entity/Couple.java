package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 情侣关系实体类
 * 绑定两个用户为情侣关系
 */
@Data
@Entity
@Table(name = "couple")
public class Couple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户A的ID */
    @Column(nullable = false)
    private Long userId1;

    /** 用户B的ID */
    @Column(nullable = false)
    private Long userId2;

    /** 在一起的日期 */
    @Column(length = 10)
    private String togetherDate;

    /** 绑定状态：0-待确认，1-已绑定，2-已解绑 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer status;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = 0;
        }
    }
}
