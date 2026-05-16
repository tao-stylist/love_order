package com.loveorder.controller;

import com.loveorder.common.Result;
import com.loveorder.dto.AnniversaryDTO;
import com.loveorder.entity.Anniversary;
import com.loveorder.service.AnniversaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 纪念日控制器
 * 处理纪念日的CRUD和查询
 */
@Tag(name = "纪念日管理", description = "纪念日相关接口")
@RestController
@RequestMapping("/api/anniversary")
@RequiredArgsConstructor
public class AnniversaryController {

    private final AnniversaryService anniversaryService;

    @Operation(summary = "获取我的纪念日列表")
    @GetMapping("/list")
    public Result<List<Anniversary>> getMyAnniversaries(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(anniversaryService.getUserAnniversaries(userId));
    }

    @Operation(summary = "根据类型获取纪念日")
    @GetMapping("/type/{type}")
    public Result<List<Anniversary>> getByType(Authentication authentication,
                                               @PathVariable Integer type) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(anniversaryService.getByType(userId, type));
    }

    @Operation(summary = "获取即将到来的纪念日")
    @GetMapping("/upcoming")
    public Result<List<Anniversary>> getUpcoming(Authentication authentication,
                                                  @RequestParam(defaultValue = "30") int days) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(anniversaryService.getUpcomingAnniversaries(userId, days));
    }

    @Operation(summary = "创建纪念日")
    @PostMapping
    public Result<Anniversary> createAnniversary(Authentication authentication,
                                                  @Valid @RequestBody AnniversaryDTO dto) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(anniversaryService.createAnniversary(userId, dto));
    }

    @Operation(summary = "更新纪念日")
    @PutMapping("/{id}")
    public Result<Anniversary> updateAnniversary(Authentication authentication,
                                                  @PathVariable Long id,
                                                  @Valid @RequestBody AnniversaryDTO dto) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(anniversaryService.updateAnniversary(userId, id, dto));
    }

    @Operation(summary = "删除纪念日")
    @DeleteMapping("/{id}")
    public Result<Void> deleteAnniversary(Authentication authentication,
                                          @PathVariable Long id) {
        Long userId = (Long) authentication.getPrincipal();
        anniversaryService.deleteAnniversary(userId, id);
        return Result.success();
    }
}
