package com.loveorder.repository;

import com.loveorder.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 菜品 Repository
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * 根据分类ID和状态查找菜品
     */
    List<MenuItem> findByCategoryIdAndStatusOrderByCreateTimeDesc(Long categoryId, Integer status);

    /**
     * 根据状态分页查找菜品
     */
    Page<MenuItem> findByStatus(Integer status, Pageable pageable);

    /**
     * 根据心情标签模糊查询菜品
     */
    @Query("SELECT m FROM MenuItem m WHERE m.status = 1 AND m.moodTags LIKE %:moodTag%")
    List<MenuItem> findByMoodTag(@Param("moodTag") String moodTag);

    /**
     * 根据名称模糊搜索菜品
     */
    List<MenuItem> findByNameContainingAndStatus(String name, Integer status);

    /**
     * 查询所有上架菜品
     */
    List<MenuItem> findByStatusOrderBySalesCountDesc(Integer status);

    /**
     * 随机查询指定数量的上架菜品
     */
    @Query(value = "SELECT * FROM menu_item WHERE status = 1 ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<MenuItem> findRandomItems(@Param("count") int count);
}
