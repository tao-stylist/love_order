package com.loveorder.service.impl;

import com.loveorder.common.BusinessException;
import com.loveorder.common.PageResult;
import com.loveorder.common.ResultCode;
import com.loveorder.entity.Notification;
import com.loveorder.repository.NotificationRepository;
import com.loveorder.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public PageResult<Notification> getUserNotifications(Long userId, int page, int size) {
        Page<Notification> pageResult = notificationRepository.findByUserIdOrderByCreateTimeDesc(
                userId, PageRequest.of(page - 1, size));
        return PageResult.of(pageResult);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, 0);
    }

    @Override
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND));

        // 验证通知归属
        if (!notification.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (notification.getIsRead() == 0) {
            notification.setIsRead(1);
            notification.setReadTime(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unreadList = notificationRepository.findByUserIdAndIsRead(userId, 0);
        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : unreadList) {
            notification.setIsRead(1);
            notification.setReadTime(now);
        }

        if (!unreadList.isEmpty()) {
            notificationRepository.saveAll(unreadList);
            log.info("批量标记已读: userId={}, count={}", userId, unreadList.size());
        }
    }

    @Override
    public void sendNotification(Long userId, String title, String content, Integer type, Long bizId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type != null ? type : 0);
        notification.setBizId(bizId);

        notificationRepository.save(notification);
        log.info("通知已发送: userId={}, title={}, type={}", userId, title, type);
    }
}
