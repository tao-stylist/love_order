package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 使用 @Table 指定表名避免 SQL 关键字冲突
 */
@Data
@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 订单编号 */
    @Column(nullable = false, unique = true, length = 32)
    private String orderNo;

    /** 用户ID（下单人） */
    @Column(nullable = false)
    private Long userId;

    /** 情侣ID */
    private Long coupleId;

    /** 订单总金额 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /** 订单状态：0-待支付，1-已支付，2-制作中，3-已完成，4-已取消 */
    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer status;

    /** 备注 */
    @Column(length = 500)
    private String remark;

    /** 心情标签（下单时选择的心情） */
    @Column(length = 50)
    private String moodTag;

    /** 用餐方式：0-堂食，1-外卖 */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    private Integer diningMode;

    /** 桌号 */
    @Column(length = 20)
    private String tableNo;

    /** 创建时间 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 完成时间 */
    private LocalDateTime completeTime;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = 0;
        }
        if (this.diningMode == null) {
            this.diningMode = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
