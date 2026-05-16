package com.loveorder.repository;

import com.loveorder.entity.Anniversary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 纪念日 Repository
 */
public interface AnniversaryRepository extends JpaRepository<Anniversary, Long> {

    /**
     * 根据用户ID查找所有纪念日
     */
    List<Anniversary> findByUserIdOrderByDateAsc(Long userId);

    /**
     * 根据用户ID和类型查找纪念日
     */
    List<Anniversary> findByUserIdAndType(Long userId, Integer type);
}
