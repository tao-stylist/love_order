package com.loveorder.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项实体类
 * 订单中的每个菜品明细
 */
@Data
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 订单ID */
    @Column(nullable = false)
    private Long orderId;

    /** 菜品ID */
    @Column(nullable = false)
    private Long menuItemId;

    /** 菜品名称（下单时快照） */
    @Column(nullable = false, length = 100)
    private String menuItemName;

    /** 菜品图片（下单时快照） */
    @Column(length = 500)
    private String menuItemImage;

    /** 单价（下单时快照） */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** 数量 */
    @Column(nullable = false)
    private Integer quantity;

    /** 小计金额 */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
