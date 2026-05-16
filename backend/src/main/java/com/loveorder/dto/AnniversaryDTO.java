package com.loveorder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 纪念日请求 DTO
 */
@Data
public class AnniversaryDTO {

    /** 纪念日标题 */
    @NotBlank(message = "标题不能为空")
    private String title;

    /** 纪念日日期（格式：yyyy-MM-dd） */
    @NotBlank(message = "日期不能为空")
    private String date;

    /** 纪念日类型：0-恋爱纪念日，1-生日，2-节日，3-自定义 */
    @NotNull(message = "类型不能为空")
    private Integer type;

    /** 是否重复：0-不重复，1-每年重复 */
    private Integer repeatable;

    /** 提前提醒天数 */
    private Integer remindDays;

    /** 备注 */
    private String remark;
}
