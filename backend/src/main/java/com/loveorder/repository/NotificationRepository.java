package com.loveorder.repository;

import com.loveorder.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 通知 Repository
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 根据用户ID分页查找通知
     */
    Page<Notification> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    /**
     * 根据用户ID和是否已读查找通知
     */
    List<Notification> findByUserIdAndIsRead(Long userId, Integer isRead);

    /**
     * 统计用户未读通知数量
     */
    long countByUserIdAndIsRead(Long userId, Integer isRead);
}
