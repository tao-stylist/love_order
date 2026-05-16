package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 纪念日实体类
 * 记录情侣的重要纪念日
 */
@Data
@Entity
@Table(name = "anniversary")
public class Anniversary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户ID */
    @Column(nullable = false)
    private Long userId;

    /** 纪念日标题 */
    @Column(nullable = false, length = 100)
    private String title;

    /** 纪念日日期 */
    @Column(nullable = false, length = 10)
    private String date;

    /** 纪念日类型：0-恋爱纪念日，1-生日，2-节日，3-自定义 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 3")
    private Integer type;

    /** 是否重复：0-不重复，1-每年重复 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Integer repeatable;

    /** 提前提醒天数 */
    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer remindDays;

    /** 备注 */
    @Column(length = 500)
    private String remark;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        if (this.type == null) {
            this.type = 3;
        }
        if (this.repeatable == null) {
            this.repeatable = 1;
        }
        if (this.remindDays == null) {
            this.remindDays = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
