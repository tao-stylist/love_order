package com.loveorder.service;

import com.loveorder.common.PageResult;
import com.loveorder.entity.MenuCategory;
import com.loveorder.entity.MenuItem;

import java.util.List;

/**
 * 菜品服务接口
 */
public interface MenuService {

    /**
     * 获取所有菜品分类
     */
    List<MenuCategory> getAllCategories();

    /**
     * 创建菜品分类
     */
    MenuCategory createCategory(MenuCategory category);

    /**
     * 分页查询菜品列表
     */
    PageResult<MenuItem> getMenuList(int page, int size);

    /**
     * 根据分类ID获取菜品
     */
    List<MenuItem> getMenuByCategory(Long categoryId);

    /**
     * 根据ID获取菜品详情
     */
    MenuItem getMenuItemById(Long id);

    /**
     * 创建菜品
     */
    MenuItem createMenuItem(MenuItem menuItem);

    /**
     * 更新菜品
     */
    MenuItem updateMenuItem(Long id, MenuItem menuItem);

    /**
     * 删除菜品（下架）
     */
    void deleteMenuItem(Long id);

    /**
     * 根据心情标签推荐菜品
     */
    List<MenuItem> recommendByMood(String moodTag);

    /**
     * 获取每日推荐菜品
     */
    List<MenuItem> getDailyRecommend();

    /**
     * 搜索菜品
     */
    List<MenuItem> searchMenu(String keyword);
}
