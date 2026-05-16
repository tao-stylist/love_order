package com.loveorder.service;

/**
 * 爱心积分服务接口
 */
public interface LovePointService {

    /**
     * 下单增加积分（+10分）
     * @param userId 用户ID
     */
    void addPointsForOrder(Long userId);

    /**
     * 评价增加积分（+5分）
     * @param userId 用户ID
     */
    void addPointsForReview(Long userId);

    /**
     * 每日签到增加积分（+1分）
     * @param userId 用户ID
     * @return 签到后的总积分
     */
    int addPointsForSignIn(Long userId);

    /**
     * 获取用户当前积分
     * @param userId 用户ID
     * @return 当前积分
     */
    int getPoints(Long userId);
}
