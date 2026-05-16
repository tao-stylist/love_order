package com.loveorder.service;

import com.loveorder.common.PageResult;
import com.loveorder.entity.Notification;

/**
 * 通知服务接口
 */
public interface NotificationService {

    /**
     * 获取用户通知列表（分页）
     */
    PageResult<Notification> getUserNotifications(Long userId, int page, int size);

    /**
     * 获取未读通知数量
     */
    long getUnreadCount(Long userId);

    /**
     * 标记通知为已读
     */
    void markAsRead(Long userId, Long notificationId);

    /**
     * 全部标记为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 发送通知
     */
    void sendNotification(Long userId, String title, String content, Integer type, Long bizId);
}
