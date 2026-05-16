package com.loveorder.service.impl;

import com.loveorder.common.BusinessException;
import com.loveorder.common.ResultCode;
import com.loveorder.dto.LoginDTO;
import com.loveorder.entity.User;
import com.loveorder.repository.UserRepository;
import com.loveorder.service.UserService;
import com.loveorder.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SIGN_IN_KEY_PREFIX = "user:signin:";
    private static final int SIGN_IN_POINTS = 1;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        String openid = loginDTO.getOpenid();

        // 查找或创建用户
        User user = userRepository.findByOpenid(openid).orElseGet(() -> {
            // 新用户自动注册
            User newUser = new User();
            newUser.setOpenid(openid);
            newUser.setNickname(loginDTO.getNickname() != null ? loginDTO.getNickname() : "用户" + openid.substring(0, 6));
            newUser.setAvatar(loginDTO.getAvatar());
            newUser.setLovePoints(0);
            log.info("新用户注册: openid={}, nickname={}", openid, newUser.getNickname());
            return userRepository.save(newUser);
        });

        // 如果传入了新的昵称或头像，更新用户信息
        if (loginDTO.getNickname() != null || loginDTO.getAvatar() != null) {
            if (loginDTO.getNickname() != null) {
                user.setNickname(loginDTO.getNickname());
            }
            if (loginDTO.getAvatar() != null) {
                user.setAvatar(loginDTO.getAvatar());
            }
            userRepository.save(user);
        }

        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getOpenid());

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("lovePoints", user.getLovePoints());

        log.info("用户登录成功: userId={}, nickname={}", user.getId(), user.getNickname());
        return result;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);

        // 更新允许修改的字段
        if (user.getNickname() != null) {
            existingUser.setNickname(user.getNickname());
        }
        if (user.getAvatar() != null) {
            existingUser.setAvatar(user.getAvatar());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public Integer dailySignIn(Long userId) {
        User user = getUserById(userId);
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 检查今天是否已签到
        String redisKey = SIGN_IN_KEY_PREFIX + userId;
        Boolean hasSigned = redisTemplate.hasKey(redisKey);

        if (Boolean.TRUE.equals(hasSigned)) {
            throw new BusinessException("今天已经签到过了");
        }

        // 增加积分
        user.setLovePoints(user.getLovePoints() + SIGN_IN_POINTS);
        user.setLastSignInDate(today);
        userRepository.save(user);

        // 在 Redis 中记录签到，设置过期时间为到当天结束
        redisTemplate.opsForValue().set(redisKey, "1", 1, TimeUnit.DAYS);

        log.info("用户签到成功: userId={}, 当前积分={}", userId, user.getLovePoints());
        return user.getLovePoints();
    }

    @Override
    public Integer getLovePoints(Long userId) {
        User user = getUserById(userId);
        return user.getLovePoints();
    }
}
