package com.loveorder.repository;

import com.loveorder.entity.DailyRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 每日推荐 Repository
 */
public interface DailyRecommendRepository extends JpaRepository<DailyRecommend, Long> {

    /**
     * 根据推荐日期查找推荐列表，按排序号升序
     */
    List<DailyRecommend> findByRecommendDateOrderBySortOrderAsc(String recommendDate);
}
