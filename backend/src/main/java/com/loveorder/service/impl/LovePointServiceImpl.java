package com.loveorder.service.impl;

import com.loveorder.common.BusinessException;
import com.loveorder.common.ResultCode;
import com.loveorder.entity.User;
import com.loveorder.repository.UserRepository;
import com.loveorder.service.LovePointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 爱心积分服务实现类
 * 积分规则：
 * - 下单成功 +10分
 * - 评价订单 +5分
 * - 每日签到 +1分
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LovePointServiceImpl implements LovePointService {

    private final UserRepository userRepository;

    /** 下单积分 */
    private static final int ORDER_POINTS = 10;
    /** 评价积分 */
    private static final int REVIEW_POINTS = 5;
    /** 签到积分 */
    private static final int SIGN_IN_POINTS = 1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPointsForOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        user.setLovePoints(user.getLovePoints() + ORDER_POINTS);
        userRepository.save(user);

        log.info("下单积分增加: userId={}, +{}分, 当前积分={}", userId, ORDER_POINTS, user.getLovePoints());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPointsForReview(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        user.setLovePoints(user.getLovePoints() + REVIEW_POINTS);
        userRepository.save(user);

        log.info("评价积分增加: userId={}, +{}分, 当前积分={}", userId, REVIEW_POINTS, user.getLovePoints());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addPointsForSignIn(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));

        user.setLovePoints(user.getLovePoints() + SIGN_IN_POINTS);
        userRepository.save(user);

        log.info("签到积分增加: userId={}, +{}分, 当前积分={}", userId, SIGN_IN_POINTS, user.getLovePoints());
        return user.getLovePoints();
    }

    @Override
    public int getPoints(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ResultCode.USER_NOT_FOUND));
        return user.getLovePoints();
    }
}
