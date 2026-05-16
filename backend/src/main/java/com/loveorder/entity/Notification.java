package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知实体类
 * 系统通知和情侣消息
 */
@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 接收用户ID */
    @Column(nullable = false)
    private Long userId;

    /** 通知标题 */
    @Column(nullable = false, length = 100)
    private String title;

    /** 通知内容 */
    @Column(nullable = false, length = 1000)
    private String content;

    /** 通知类型：0-系统通知，1-订单通知，2-纪念日提醒，3-情侣消息 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer type;

    /** 是否已读：0-未读，1-已读 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer isRead;

    /** 关联的业务ID（如订单ID、纪念日ID等） */
    private Long bizId;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /** 读取时间 */
    private LocalDateTime readTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.type == null) {
            this.type = 0;
        }
        if (this.isRead == null) {
            this.isRead = 0;
        }
    }
}
