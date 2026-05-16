package com.loveorder.controller;

import com.loveorder.common.Result;
import com.loveorder.entity.User;
import com.loveorder.service.LovePointService;
import com.loveorder.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 处理个人信息、爱心积分等
 */
@Tag(name = "用户管理", description = "用户信息和积分相关接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LovePointService lovePointService;

    @Operation(summary = "获取个人信息")
    @GetMapping("/info")
    public Result<User> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(userService.getUserById(userId));
    }

    @Operation(summary = "更新个人信息")
    @PutMapping("/info")
    public Result<User> updateUserInfo(Authentication authentication,
                                       @RequestBody User user) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(userService.updateUser(userId, user));
    }

    @Operation(summary = "获取爱心积分")
    @GetMapping("/points")
    public Result<Integer> getLovePoints(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(lovePointService.getPoints(userId));
    }

    @Operation(summary = "每日签到")
    @PostMapping("/signin")
    public Result<Integer> dailySignIn(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Integer points = userService.dailySignIn(userId);
        return Result.success("签到成功", points);
    }
}
