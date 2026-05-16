package com.loveorder.common;

import lombok.Getter;

/**
 * 响应码枚举
 * 统一管理所有接口的响应状态码和消息
 */
@Getter
public enum ResultCode {

    /** 成功 */
    SUCCESS(200, "操作成功"),

    /** 通用错误 */
    ERROR(500, "服务器内部错误"),

    /** 参数校验错误 */
    PARAM_ERROR(400, "参数校验失败"),

    /** 未认证 */
    UNAUTHORIZED(401, "未登录或Token已过期"),

    /** 无权限 */
    FORBIDDEN(403, "无权限访问"),

    /** 资源未找到 */
    NOT_FOUND(404, "资源不存在"),

    /** 用户相关 */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    OPENID_INVALID(1004, "OpenID无效"),

    /** 菜品相关 */
    MENU_NOT_FOUND(2001, "菜品不存在"),
    CATEGORY_NOT_FOUND(2002, "分类不存在"),

    /** 订单相关 */
    ORDER_NOT_FOUND(3001, "订单不存在"),
    ORDER_STATUS_ERROR(3002, "订单状态异常"),
    ORDER_CANNOT_CANCEL(3003, "订单无法取消"),

    /** 评价相关 */
    REVIEW_NOT_FOUND(4001, "评价不存在"),
    REVIEW_ALREADY_EXISTS(4002, "已评价，请勿重复评价"),

    /** 纪念日相关 */
    ANNIVERSARY_NOT_FOUND(5001, "纪念日不存在"),

    /** 积分相关 */
    POINTS_NOT_ENOUGH(6001, "积分不足"),

    /** 情侣关系 */
    COUPLE_NOT_FOUND(7001, "未绑定情侣关系"),
    COUPLE_ALREADY_BOUND(7002, "已绑定情侣关系"),

    /** 文件上传 */
    FILE_UPLOAD_ERROR(8001, "文件上传失败"),
    FILE_TYPE_ERROR(8002, "文件类型不支持");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
