package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 微信OpenID */
    @Column(nullable = false, unique = true, length = 64)
    private String openid;

    /** 昵称 */
    @Column(length = 50)
    private String nickname;

    /** 头像URL */
    @Column(length = 500)
    private String avatar;

    /** 手机号 */
    @Column(length = 20)
    private String phone;

    /** 爱心积分 */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer lovePoints;

    /** 最后签到日期 */
    @Column(length = 10)
    private String lastSignInDate;

    /** 绑定的另一半用户ID */
    @Column
    private Long partnerId;

    /** 绑定码，用于互相绑定 */
    @Column(length = 12, unique = true)
    private String bindCode;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        if (this.lovePoints == null) {
            this.lovePoints = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
