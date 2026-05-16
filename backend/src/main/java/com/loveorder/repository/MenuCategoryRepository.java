package com.loveorder.repository;

import com.loveorder.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 菜品分类 Repository
 */
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {

    /**
     * 根据状态查找所有分类，按排序号升序
     */
    List<MenuCategory> findByStatusOrderBySortOrderAsc(Integer status);
}
