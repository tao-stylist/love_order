package com.loveorder.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 * @param <T> 数据类型
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页码 */
    private long page;

    /** 每页大小 */
    private long size;

    /** 总记录数 */
    private long total;

    /** 总页数 */
    private long totalPages;

    /** 数据列表 */
    private List<T> records;

    public PageResult() {
    }

    public PageResult(long page, long size, long total, List<T> records) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = (total + size - 1) / size;
        this.records = records;
    }

    /**
     * 从 Spring Data Page 对象构建分页结果
     */
    public static <T> PageResult<T> of(org.springframework.data.domain.Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setPage(page.getNumber() + 1);  // Spring Data 页码从0开始，转为从1开始
        result.setSize(page.getSize());
        result.setTotal(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setRecords(page.getContent());
        return result;
    }
}
