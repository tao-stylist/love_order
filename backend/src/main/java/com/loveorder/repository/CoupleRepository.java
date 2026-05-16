package com.loveorder.repository;

import com.loveorder.entity.Couple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 情侣关系 Repository
 */
public interface CoupleRepository extends JpaRepository<Couple, Long> {

    /**
     * 根据用户ID查找情侣关系（任一方）
     */
    List<Couple> findByUserId1OrUserId2AndStatus(Long userId1, Long userId2, Integer status);

    /**
     * 根据用户ID和状态查找情侣关系
     */
    Optional<Couple> findByUserId1AndStatusOrUserId2AndStatus(Long userId1, Integer status1, Long userId2, Integer status2);
}
