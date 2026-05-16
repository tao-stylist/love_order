package com.loveorder.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 下单请求 DTO
 */
@Data
public class OrderDTO {

    /** 心情标签 */
    private String moodTag;

    /** 用餐方式：0-堂食，1-外卖 */
    private Integer diningMode;

    /** 桌号 */
    private String tableNo;

    /** 备注 */
    private String remark;

    /** 订单项列表 */
    @NotEmpty(message = "订单项不能为空")
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {

        /** 菜品ID */
        @NotNull(message = "菜品ID不能为空")
        private Long menuItemId;

        /** 数量 */
        @NotNull(message = "数量不能为空")
        private Integer quantity;
    }
}
