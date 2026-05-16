<template>
  <!-- 菜单页 - 甜蜜菜单 -->
  <view class="page-menu">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <view class="search-input-wrapper">
        <text class="search-icon">🔍</text>
        <input
          class="search-input"
          type="text"
          placeholder="搜索你想吃的美食..."
          placeholder-class="search-placeholder"
          v-model="orderStore.searchKeyword"
          confirm-type="search"
        />
        <text
          v-if="orderStore.searchKeyword"
          class="search-clear"
          @click="orderStore.searchKeyword = ''"
        >✕</text>
      </view>
    </view>

    <!-- 主体内容：左侧分类 + 右侧菜品列表 -->
    <view class="menu-content">
      <!-- 左侧分类导航 -->
      <scroll-view scroll-y class="category-sidebar" :show-scrollbar="false">
        <view
          v-for="cat in orderStore.categories"
          :key="cat.id"
          class="category-item"
          :class="{ active: orderStore.currentCategory === cat.id }"
          @click="selectCategory(cat.id)"
        >
          <text class="category-icon">{{ cat.icon }}</text>
          <text class="category-name">{{ cat.name }}</text>
          <!-- 选中指示条 -->
          <view v-if="orderStore.currentCategory === cat.id" class="category-indicator"></view>
        </view>
      </scroll-view>

      <!-- 右侧菜品列表 -->
      <scroll-view scroll-y class="menu-list" :show-scrollbar="false">
        <!-- 分类标题 -->
        <view class="list-header">
          <text class="list-title">{{ currentCategoryName }}</text>
          <text class="list-count">共{{ filteredList.length }}道菜</text>
        </view>

        <!-- 菜品列表 -->
        <view v-if="filteredList.length > 0" class="menu-items">
          <MenuItem
            v-for="item in filteredList"
            :key="item.id"
            :item="item"
            @add-cart="handleAddCart"
            @detail="handleDetail"
          />
        </view>

        <!-- 空状态 -->
        <EmptyState
          v-else
          icon="🍽️"
          title="暂无菜品"
          description="换个分类看看吧~"
        />

        <!-- 底部占位 -->
        <view style="height: 200rpx;"></view>
      </scroll-view>
    </view>

    <!-- 购物车浮动按钮 -->
    <CartFloat />
  </view>
</template>

<script setup>
/**
 * 菜单页 - 甜蜜菜单
 * 左侧分类导航 + 右侧菜品列表 + 购物车
 */
import { computed } from 'vue'
import { useOrderStore } from '../../store/order.js'
import MenuItem from '../../components/MenuItem/MenuItem.vue'
import CartFloat from '../../components/CartFloat/CartFloat.vue'
import EmptyState from '../../components/EmptyState/EmptyState.vue'

const orderStore = useOrderStore()

// 当前分类名称
const currentCategoryName = computed(() => {
  const cat = orderStore.categories.find(c => c.id === orderStore.currentCategory)
  return cat ? cat.name : '推荐'
})

// 筛选后的菜品列表
const filteredList = computed(() => orderStore.filteredMenuList)

/**
 * 选择分类
 */
function selectCategory(categoryId) {
  orderStore.currentCategory = categoryId
}

/**
 * 加入购物车
 */
function handleAddCart(item) {
  orderStore.addToCart(item)
}

/**
 * 查看菜品详情
 */
function handleDetail(item) {
  uni.showToast({
    title: `${item.name} - ${item.desc}`,
    icon: 'none',
    duration: 2000
  })
}
</script>

<style lang="scss" scoped>
.page-menu {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #FFF5F7;
}

/* 搜索栏 */
.search-bar {
  padding: 16rpx 24rpx;
  background: #ffffff;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  background: #FFF5F7;
  border-radius: 40rpx;
  padding: 16rpx 24rpx;
  gap: 12rpx;

  .search-icon {
    font-size: 28rpx;
    flex-shrink: 0;
  }

  .search-input {
    flex: 1;
    font-size: 26rpx;
    color: #333333;
    height: 40rpx;
  }

  .search-clear {
    font-size: 24rpx;
    color: #999999;
    padding: 4rpx 8rpx;
  }
}

.search-placeholder {
  color: #cccccc;
  font-size: 26rpx;
}

/* 主体内容 */
.menu-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* 左侧分类导航 */
.category-sidebar {
  width: 180rpx;
  background: #ffffff;
  flex-shrink: 0;
  height: 100%;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 12rpx;
  position: relative;
  transition: all 0.3s ease;

  &.active {
    background: #FFF5F7;

    .category-name {
      color: #FF6B9D;
      font-weight: 600;
    }

    .category-icon {
      transform: scale(1.15);
    }
  }

  &:active {
    background: #FFF0F3;
  }
}

.category-icon {
  font-size: 36rpx;
  margin-bottom: 8rpx;
  transition: transform 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.category-name {
  font-size: 24rpx;
  color: #666666;
  transition: all 0.3s ease;
}

.category-indicator {
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 6rpx;
  height: 40rpx;
  background: linear-gradient(180deg, #FF6B9D, #FFA07A);
  border-radius: 0 3rpx 3rpx 0;
}

/* 右侧菜品列表 */
.menu-list {
  flex: 1;
  padding: 0 16rpx;
  height: 100%;
}

.list-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 8rpx 16rpx;

  .list-title {
    font-size: 32rpx;
    font-weight: 700;
    color: #333333;
  }

  .list-count {
    font-size: 22rpx;
    color: #999999;
  }
}

.menu-items {
  display: flex;
  flex-direction: column;
}
</style>
