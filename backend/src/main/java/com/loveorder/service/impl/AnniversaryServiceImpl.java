package com.loveorder.service.impl;

import com.loveorder.common.BusinessException;
import com.loveorder.common.ResultCode;
import com.loveorder.dto.AnniversaryDTO;
import com.loveorder.entity.Anniversary;
import com.loveorder.repository.AnniversaryRepository;
import com.loveorder.service.AnniversaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 纪念日服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnniversaryServiceImpl implements AnniversaryService {

    private final AnniversaryRepository anniversaryRepository;

    @Override
    public List<Anniversary> getUserAnniversaries(Long userId) {
        return anniversaryRepository.findByUserIdOrderByDateAsc(userId);
    }

    @Override
    public List<Anniversary> getByType(Long userId, Integer type) {
        return anniversaryRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public Anniversary createAnniversary(Long userId, AnniversaryDTO dto) {
        Anniversary anniversary = new Anniversary();
        anniversary.setUserId(userId);
        anniversary.setTitle(dto.getTitle());
        anniversary.setDate(dto.getDate());
        anniversary.setType(dto.getType());
        anniversary.setRepeatable(dto.getRepeatable() != null ? dto.getRepeatable() : 1);
        anniversary.setRemindDays(dto.getRemindDays() != null ? dto.getRemindDays() : 1);
        anniversary.setRemark(dto.getRemark());

        Anniversary saved = anniversaryRepository.save(anniversary);
        log.info("纪念日创建成功: userId={}, title={}, date={}", userId, dto.getTitle(), dto.getDate());
        return saved;
    }

    @Override
    public Anniversary updateAnniversary(Long userId, Long id, AnniversaryDTO dto) {
        Anniversary anniversary = anniversaryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ANNIVERSARY_NOT_FOUND));

        // 验证归属
        if (!anniversary.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (dto.getTitle() != null) {
            anniversary.setTitle(dto.getTitle());
        }
        if (dto.getDate() != null) {
            anniversary.setDate(dto.getDate());
        }
        if (dto.getType() != null) {
            anniversary.setType(dto.getType());
        }
        if (dto.getRepeatable() != null) {
            anniversary.setRepeatable(dto.getRepeatable());
        }
        if (dto.getRemindDays() != null) {
            anniversary.setRemindDays(dto.getRemindDays());
        }
        if (dto.getRemark() != null) {
            anniversary.setRemark(dto.getRemark());
        }

        return anniversaryRepository.save(anniversary);
    }

    @Override
    public void deleteAnniversary(Long userId, Long id) {
        Anniversary anniversary = anniversaryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ANNIVERSARY_NOT_FOUND));

        if (!anniversary.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        anniversaryRepository.delete(anniversary);
        log.info("纪念日已删除: userId={}, anniversaryId={}", userId, id);
    }

    @Override
    public List<Anniversary> getUpcomingAnniversaries(Long userId, int days) {
        List<Anniversary> allAnniversaries = anniversaryRepository.findByUserIdOrderByDateAsc(userId);
        LocalDate today = LocalDate.now();

        return allAnniversaries.stream()
                .filter(a -> {
                    // 解析纪念日日期（月-日）
                    String dateStr = a.getDate();
                    try {
                        LocalDate anniversaryDate = LocalDate.parse(dateStr);
                        // 对于每年重复的纪念日，计算今年对应的日期
                        if (a.getRepeatable() == 1) {
                            LocalDate thisYearDate = LocalDate.of(today.getYear(),
                                    anniversaryDate.getMonthValue(), anniversaryDate.getDayOfMonth());
                            // 如果今年已过，看明年
                            if (thisYearDate.isBefore(today)) {
                                thisYearDate = thisYearDate.plusYears(1);
                            }
                            long daysUntil = ChronoUnit.DAYS.between(today, thisYearDate);
                            return daysUntil <= days;
                        } else {
                            long daysUntil = ChronoUnit.DAYS.between(today, anniversaryDate);
                            return daysUntil >= 0 && daysUntil <= days;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
