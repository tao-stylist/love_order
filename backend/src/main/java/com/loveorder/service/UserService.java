package com.loveorder.service;

import com.loveorder.dto.LoginDTO;
import com.loveorder.entity.User;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 微信小程序登录（模拟OpenID登录）
     * 如果用户不存在则自动注册
     * @param loginDTO 登录请求
     * @return 包含 token 和用户信息的 Map
     */
    Map<String, Object> login(LoginDTO loginDTO);

    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);

    /**
     * 更新用户信息
     */
    User updateUser(Long id, User user);

    /**
     * 每日签到，增加爱心积分
     * @param userId 用户ID
     * @return 签到后的积分
     */
    Integer dailySignIn(Long userId);

    /**
     * 获取用户爱心积分
     */
    Integer getLovePoints(Long userId);
}
