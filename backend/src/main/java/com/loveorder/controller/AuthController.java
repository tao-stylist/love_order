package com.loveorder.controller;

import com.loveorder.common.Result;
import com.loveorder.dto.LoginDTO;
import com.loveorder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录/注册
 */
@Tag(name = "认证管理", description = "登录注册相关接口")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 微信小程序登录（模拟OpenID登录）
     * 如果用户不存在则自动注册
     */
    @Operation(summary = "微信登录", description = "通过OpenID登录或注册")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> result = userService.login(loginDTO);
        return Result.success(result);
    }
}
