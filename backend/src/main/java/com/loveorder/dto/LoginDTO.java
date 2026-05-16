package com.loveorder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 */
@Data
public class LoginDTO {

    /** 微信OpenID（模拟登录使用） */
    @NotBlank(message = "OpenID不能为空")
    private String openid;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;
}
