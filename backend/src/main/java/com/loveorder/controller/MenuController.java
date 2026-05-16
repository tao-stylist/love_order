package com.loveorder.controller;

import com.loveorder.common.PageResult;
import com.loveorder.common.Result;
import com.loveorder.entity.MenuCategory;
import com.loveorder.entity.MenuItem;
import com.loveorder.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品控制器
 * 处理菜品和分类的CRUD、心情点单推荐、每日推荐
 */
@Tag(name = "菜品管理", description = "菜品和分类相关接口")
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // ========== 分类相关 ==========

    @Operation(summary = "获取所有分类")
    @GetMapping("/category/list")
    public Result<List<MenuCategory>> getAllCategories() {
        return Result.success(menuService.getAllCategories());
    }

    @Operation(summary = "创建分类")
    @PostMapping("/category")
    public Result<MenuCategory> createCategory(@RequestBody MenuCategory category) {
        return Result.success(menuService.createCategory(category));
    }

    // ========== 菜品相关 ==========

    @Operation(summary = "分页查询菜品列表")
    @GetMapping("/list")
    public Result<PageResult<MenuItem>> getMenuList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(menuService.getMenuList(page, size));
    }

    @Operation(summary = "根据分类获取菜品")
    @GetMapping("/category/{categoryId}")
    public Result<List<MenuItem>> getMenuByCategory(@PathVariable Long categoryId) {
        return Result.success(menuService.getMenuByCategory(categoryId));
    }

    @Operation(summary = "获取菜品详情")
    @GetMapping("/{id}")
    public Result<MenuItem> getMenuItemById(@PathVariable Long id) {
        return Result.success(menuService.getMenuItemById(id));
    }

    @Operation(summary = "创建菜品")
    @PostMapping
    public Result<MenuItem> createMenuItem(@RequestBody MenuItem menuItem) {
        return Result.success(menuService.createMenuItem(menuItem));
    }

    @Operation(summary = "更新菜品")
    @PutMapping("/{id}")
    public Result<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        return Result.success(menuService.updateMenuItem(id, menuItem));
    }

    @Operation(summary = "删除菜品（下架）")
    @DeleteMapping("/{id}")
    public Result<Void> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return Result.success();
    }

    @Operation(summary = "搜索菜品")
    @GetMapping("/search")
    public Result<List<MenuItem>> searchMenu(@RequestParam String keyword) {
        return Result.success(menuService.searchMenu(keyword));
    }

    // ========== 推荐相关 ==========

    @Operation(summary = "心情点单推荐", description = "根据心情标签推荐菜品")
    @GetMapping("/mood/{moodTag}")
    public Result<List<MenuItem>> recommendByMood(@PathVariable String moodTag) {
        return Result.success(menuService.recommendByMood(moodTag));
    }

    @Operation(summary = "每日推荐", description = "获取今日推荐菜品")
    @GetMapping("/recommend")
    public Result<List<MenuItem>> getDailyRecommend() {
        return Result.success(menuService.getDailyRecommend());
    }
}
