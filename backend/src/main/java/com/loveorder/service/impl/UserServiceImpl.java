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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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

        // 检查今天是否已签到（通过 lastSignInDate 字段判断）
        if (today.equals(user.getLastSignInDate())) {
            throw new BusinessException("今天已经签到过了");
        }

        // 增加积分
        user.setLovePoints(user.getLovePoints() + SIGN_IN_POINTS);
        user.setLastSignInDate(today);
        userRepository.save(user);

        log.info("用户签到成功: userId={}, 当前积分={}", userId, user.getLovePoints());
        return user.getLovePoints();
    }

    @Override
    public Integer getLovePoints(Long userId) {
        User user = getUserById(userId);
        return user.getLovePoints();
    }

    @Override
    public Map<String, Object> phoneLogin(String phone, String nickname) {
        // 查找或创建用户
        User user = userRepository.findByPhone(phone).orElseGet(() -> {
            // 新用户自动注册
            User newUser = new User();
            newUser.setOpenid("phone_" + phone); // 使用phone作为openid
            newUser.setPhone(phone);
            newUser.setNickname(nickname != null ? nickname : "用户" + phone.substring(phone.length() - 4));
            newUser.setLovePoints(0);
            newUser.setBindCode(generateRandomBindCode());
            log.info("新用户注册（手机号）: phone={}, nickname={}", phone, newUser.getNickname());
            return userRepository.save(newUser);
        });

        // 如果用户已有昵称，就不更新了
        if (user.getNickname() == null && nickname != null) {
            user.setNickname(nickname);
            userRepository.save(user);
        }

        // 如果用户没有绑定码，生成一个
        if (user.getBindCode() == null) {
            user.setBindCode(generateRandomBindCode());
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
        result.put("phone", user.getPhone());
        result.put("bindCode", user.getBindCode());
        result.put("partnerId", user.getPartnerId());

        log.info("用户登录成功（手机号）: userId={}, phone={}", user.getId(), phone);
        return result;
    }

    @Override
    public String generateBindCode(Long userId) {
        User user = getUserById(userId);
        if (user.getBindCode() != null) {
            return user.getBindCode();
        }
        String bindCode = generateRandomBindCode();
        user.setBindCode(bindCode);
        userRepository.save(user);
        return bindCode;
    }

    @Override
    public Map<String, Object> bindPartner(Long userId, String bindCode) {
        User currentUser = getUserById(userId);

        // 查找绑定码对应的用户
        User partner = userRepository.findByBindCode(bindCode)
                .orElseThrow(() -> new BusinessException("绑定码无效"));

        // 不能绑定自己
        if (partner.getId().equals(userId)) {
            throw new BusinessException("不能绑定自己");
        }

        // 检查是否已经绑定了其他人
        if (currentUser.getPartnerId() != null) {
            throw new BusinessException("您已经绑定过了");
        }
        if (partner.getPartnerId() != null) {
            throw new BusinessException("对方已经绑定过了");
        }

        // 互相绑定
        currentUser.setPartnerId(partner.getId());
        partner.setPartnerId(currentUser.getId());
        userRepository.save(currentUser);
        userRepository.save(partner);

        // 返回双方信息
        Map<String, Object> result = new HashMap<>();
        result.put("currentUser", convertUserToMap(currentUser));
        result.put("partner", convertUserToMap(partner));

        log.info("用户绑定成功: userId={} <-> partnerId={}", userId, partner.getId());
        return result;
    }

    @Override
    public User getPartner(Long userId) {
        User user = getUserById(userId);
        if (user.getPartnerId() == null) {
            return null;
        }
        return userRepository.findById(user.getPartnerId()).orElse(null);
    }

    /**
     * 生成随机绑定码
     */
    private String generateRandomBindCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去掉容易混淆的字符
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        String code = sb.toString();
        // 检查是否已存在，如果存在就重新生成
        if (userRepository.existsByBindCode(code)) {
            return generateRandomBindCode();
        }
        return code;
    }

    /**
     * 转换用户为Map
     */
    private Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("nickname", user.getNickname());
        map.put("avatar", user.getAvatar());
        map.put("phone", user.getPhone());
        map.put("lovePoints", user.getLovePoints());
        map.put("bindCode", user.getBindCode());
        return map;
    }
}
