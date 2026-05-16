package com.loveorder.service.impl;

import com.loveorder.common.BusinessException;
import com.loveorder.common.PageResult;
import com.loveorder.common.ResultCode;
import com.loveorder.entity.DailyRecommend;
import com.loveorder.entity.MenuCategory;
import com.loveorder.entity.MenuItem;
import com.loveorder.repository.DailyRecommendRepository;
import com.loveorder.repository.MenuCategoryRepository;
import com.loveorder.repository.MenuItemRepository;
import com.loveorder.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final DailyRecommendRepository dailyRecommendRepository;

    @Override
    public List<MenuCategory> getAllCategories() {
        return menuCategoryRepository.findByStatusOrderBySortOrderAsc(1);
    }

    @Override
    public MenuCategory createCategory(MenuCategory category) {
        return menuCategoryRepository.save(category);
    }

    @Override
    public PageResult<MenuItem> getMenuList(int page, int size) {
        Page<MenuItem> pageResult = menuItemRepository.findByStatus(1, PageRequest.of(page - 1, size));
        return PageResult.of(pageResult);
    }

    @Override
    public List<MenuItem> getMenuByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryIdAndStatusOrderByCreateTimeDesc(categoryId, 1);
    }

    @Override
    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.MENU_NOT_FOUND));
    }

    @Override
    public MenuItem createMenuItem(MenuItem menuItem) {
        menuItem.setStatus(1);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(Long id, MenuItem menuItem) {
        MenuItem existing = getMenuItemById(id);

        if (menuItem.getName() != null) {
            existing.setName(menuItem.getName());
        }
        if (menuItem.getDescription() != null) {
            existing.setDescription(menuItem.getDescription());
        }
        if (menuItem.getImage() != null) {
            existing.setImage(menuItem.getImage());
        }
        if (menuItem.getPrice() != null) {
            existing.setPrice(menuItem.getPrice());
        }
        if (menuItem.getCategoryId() != null) {
            existing.setCategoryId(menuItem.getCategoryId());
        }
        if (menuItem.getMoodTags() != null) {
            existing.setMoodTags(menuItem.getMoodTags());
        }
        if (menuItem.getRecommendScore() != null) {
            existing.setRecommendScore(menuItem.getRecommendScore());
        }
        if (menuItem.getStatus() != null) {
            existing.setStatus(menuItem.getStatus());
        }

        return menuItemRepository.save(existing);
    }

    @Override
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = getMenuItemById(id);
        menuItem.setStatus(0); // 逻辑删除（下架）
        menuItemRepository.save(menuItem);
        log.info("菜品已下架: id={}, name={}", id, menuItem.getName());
    }

    @Override
    public List<MenuItem> recommendByMood(String moodTag) {
        // 根据心情标签模糊查询菜品
        List<MenuItem> items = menuItemRepository.findByMoodTag(moodTag);

        // 如果没有匹配结果，返回推荐指数最高的菜品
        if (items.isEmpty()) {
            log.info("未找到匹配心情 '{}' 的菜品，返回热门推荐", moodTag);
            List<MenuItem> allItems = menuItemRepository.findByStatusOrderBySalesCountDesc(1);
            return allItems.stream().limit(5).collect(Collectors.toList());
        }

        // 按推荐指数降序排序
        items.sort((a, b) -> b.getRecommendScore() - a.getRecommendScore());
        return items;
    }

    @Override
    public List<MenuItem> getDailyRecommend() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 先查询今日推荐表
        List<DailyRecommend> recommends = dailyRecommendRepository.findByRecommendDateOrderBySortOrderAsc(today);

        if (!recommends.isEmpty()) {
            // 从推荐表中获取菜品ID，查询菜品详情
            List<Long> menuItemIds = recommends.stream()
                    .map(DailyRecommend::getMenuItemId)
                    .collect(Collectors.toList());
            return menuItemRepository.findAllById(menuItemIds);
        }

        // 如果今日推荐表为空，随机推荐菜品
        log.info("今日推荐表为空，随机推荐菜品");
        List<MenuItem> randomItems = menuItemRepository.findRandomItems(5);

        // 将随机推荐结果保存到推荐表，供后续查询使用
        if (!randomItems.isEmpty()) {
            List<DailyRecommend> newRecommends = new ArrayList<>();
            for (int i = 0; i < randomItems.size(); i++) {
                DailyRecommend recommend = new DailyRecommend();
                recommend.setMenuItemId(randomItems.get(i).getId());
                recommend.setRecommendDate(today);
                recommend.setReason("今日精选推荐");
                recommend.setSortOrder(i);
                newRecommends.add(recommend);
            }
            dailyRecommendRepository.saveAll(newRecommends);
        }

        return randomItems;
    }

    @Override
    public List<MenuItem> searchMenu(String keyword) {
        return menuItemRepository.findByNameContainingAndStatus(keyword, 1);
    }
}
