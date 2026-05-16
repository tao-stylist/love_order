package com.loveorder.service;

import com.loveorder.dto.AnniversaryDTO;
import com.loveorder.entity.Anniversary;

import java.util.List;

/**
 * 纪念日服务接口
 */
public interface AnniversaryService {

    /**
     * 获取用户的所有纪念日
     */
    List<Anniversary> getUserAnniversaries(Long userId);

    /**
     * 根据类型获取纪念日
     */
    List<Anniversary> getByType(Long userId, Integer type);

    /**
     * 创建纪念日
     */
    Anniversary createAnniversary(Long userId, AnniversaryDTO dto);

    /**
     * 更新纪念日
     */
    Anniversary updateAnniversary(Long userId, Long id, AnniversaryDTO dto);

    /**
     * 删除纪念日
     */
    void deleteAnniversary(Long userId, Long id);

    /**
     * 获取即将到来的纪念日（指定天数内）
     */
    List<Anniversary> getUpcomingAnniversaries(Long userId, int days);
}
