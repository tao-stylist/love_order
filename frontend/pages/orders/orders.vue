<template>
  <!-- 订单列表页 -->
  <view class="page-orders">
    <!-- Tab切换 -->
    <view class="tabs-container">
      <scroll-view scroll-x class="tabs-scroll" :show-scrollbar="false">
        <view class="tabs-list">
          <view
            v-for="tab in tabs"
            :key="tab.key"
            class="tab-item"
            :class="{ active: currentTab === tab.key }"
            @click="switchTab(tab.key)"
          >
            <text class="tab-text">{{ tab.label }}</text>
            <view v-if="currentTab === tab.key" class="tab-underline"></view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 订单列表 -->
    <scroll-view
      scroll-y
      class="order-list"
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="loadMore"
    >
      <!-- 加载中 -->
      <Loading v-if="loading && orderList.length === 0" text="加载订单中..." />

      <!-- 订单卡片列表 -->
      <view v-if="orderList.length > 0" class="order-cards">
        <view
          v-for="order in orderList"
          :key="order.id"
          class="order-card"
          @click="goDetail(order)"
        >
          <!-- 卡片头部 -->
          <view class="card-header">
            <text class="order-no">订单号：{{ order.orderNo }}</text>
            <text class="order-status" :class="'status-' + order.status">
              {{ getStatusIcon(order.status) }} {{ order.statusText }}
            </text>
          </view>

          <!-- 菜品列表 -->
          <view class="card-items">
            <view v-for="item in order.items" :key="item.menuId" class="card-item">
              <text class="item-name">{{ item.name }}</text>
              <text class="item-quantity">x{{ item.quantity }}</text>
              <text class="item-price">¥{{ (item.price * item.quantity).toFixed(1) }}</text>
            </view>
          </view>

          <!-- 卡片底部 -->
          <view class="card-footer">
            <text class="order-time">{{ formatTime(order.createTime) }}</text>
            <view class="card-footer-right">
              <text class="order-total">合计：</text>
              <text class="order-total-price">¥{{ order.totalPrice.toFixed(1) }}</text>
            </view>
          </view>

          <!-- 操作按钮 -->
          <view v-if="order.status === 'completed'" class="card-actions">
            <view class="action-btn review-btn" @click.stop="goReview(order)">
              <text>💕 评价</text>
            </view>
            <view class="action-btn reorder-btn" @click.stop="reorder(order)">
              <text>🔄 再来一单</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 空状态 -->
      <EmptyState
        v-if="!loading && orderList.length === 0"
        icon="📭"
        title="暂无订单"
        description="快去点一份爱心美食吧~"
        action-text="去点单"
        @action="goMenu"
      />

      <!-- 加载更多 -->
      <view v-if="orderList.length > 0" class="load-more">
        <text v-if="hasMore" class="load-more-text">上拉加载更多...</text>
        <text v-else class="load-more-text">没有更多了~</text>
      </view>

      <!-- 底部占位 -->
      <view style="height: 40rpx;"></view>
    </scroll-view>
  </view>
</template>

<script setup>
/**
 * 订单列表页
 * Tab切换、订单卡片、下拉刷新、上拉加载
 */
import { ref, computed, onMounted } from 'vue'
import { onShow, onPullDownRefresh } from '@dcloudio/uni-app'
import { useOrderStore } from '../../store/order.js'
import { getTimeAgo } from '../../utils/date.js'
import Loading from '../../components/Loading/Loading.vue'
import EmptyState from '../../components/EmptyState/EmptyState.vue'

const orderStore = useOrderStore()

// Tab列表
const tabs = ref([
  { key: 'all', label: '全部' },
  { key: 'pending', label: '待接单' },
  { key: 'making', label: '制作中' },
  { key: 'delivering', label: '配送中' },
  { key: 'completed', label: '已完成' }
])

// 当前Tab
const currentTab = ref('all')
// 加载状态
const loading = ref(false)
// 下拉刷新
const refreshing = ref(false)
// 是否还有更多
const hasMore = ref(true)

// 订单列表（根据tab筛选）
const orderList = computed(() => {
  if (currentTab.value === 'all') {
    return orderStore.orderList
  }
  return orderStore.orderList.filter(o => o.status === currentTab.value)
})

onMounted(() => {
  loadOrders()
})

/**
 * 加载订单
 */
function loadOrders(refresh = false) {
  loading.value = true
  orderStore.getOrders(currentTab.value, refresh)
  setTimeout(() => {
    loading.value = false
    refreshing.value = false
  }, 800)
}

/**
 * 切换Tab
 */
function switchTab(key) {
  currentTab.value = key
  loadOrders(true)
}

/**
 * 下拉刷新
 */
function onRefresh() {
  refreshing.value = true
  loadOrders(true)
}

/**
 * 上拉加载更多
 */
function loadMore() {
  if (!hasMore.value) return
  // 模拟加载更多
  hasMore.value = false
}

/**
 * 跳转到订单详情
 */
function goDetail(order) {
  orderStore.setCurrentOrder(order)
  uni.navigateTo({
    url: `/pages/orders/detail?id=${order.id}`
  })
}

/**
 * 跳转到评价页
 */
function goReview(order) {
  uni.navigateTo({
    url: `/pages/review/review?orderId=${order.id}`
  })
}

/**
 * 再来一单
 */
function reorder(order) {
  // 将订单中的菜品重新加入购物车
  order.items.forEach(item => {
    orderStore.addToCart({
      id: item.menuId,
      name: item.name,
      price: item.price,
      desc: ''
    })
  })
  uni.switchTab({
    url: '/pages/menu/menu'
  })
}

/**
 * 跳转到菜单页
 */
function goMenu() {
  uni.switchTab({
    url: '/pages/menu/menu'
  })
}

/**
 * 获取状态图标
 */
function getStatusIcon(status) {
  const iconMap = {
    'pending': '⏳',
    'accepted': '✅',
    'making': '👨‍🍳',
    'delivering': '🚴',
    'completed': '✨'
  }
  return iconMap[status] || '📋'
}

/**
 * 格式化时间
 */
function formatTime(time) {
  return getTimeAgo(time)
}
</script>

<style lang="scss" scoped>
.page-orders {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #FFF5F7;
}

/* Tab切换 */
.tabs-container {
  background: #ffffff;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
}

.tabs-scroll {
  white-space: nowrap;
}

.tabs-list {
  display: inline-flex;
  padding: 0 16rpx;
}

.tab-item {
  position: relative;
  padding: 24rpx 28rpx;

  &.active {
    .tab-text {
      color: #FF6B9D;
      font-weight: 600;
    }
  }

  .tab-text {
    font-size: 28rpx;
    color: #666666;
    transition: all 0.3s ease;
  }

  .tab-underline {
    position: absolute;
    bottom: 8rpx;
    left: 50%;
    transform: translateX(-50%);
    width: 40rpx;
    height: 4rpx;
    background: linear-gradient(90deg, #FF6B9D, #FFA07A);
    border-radius: 2rpx;
    animation: fadeIn 0.3s ease;
  }
}

/* 订单列表 */
.order-list {
  flex: 1;
  padding: 16rpx 24rpx;
}

.order-cards {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

/* 订单卡片 */
.order-card {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
  animation: fadeIn 0.4s ease;

  &:active {
    transform: scale(0.99);
  }
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #FFF0F3;

  .order-no {
    font-size: 24rpx;
    color: #999999;
  }

  .order-status {
    font-size: 26rpx;
    font-weight: 600;

    &.status-pending { color: #FFA07A; }
    &.status-making { color: #4ECDC4; }
    &.status-delivering { color: #45B7D1; }
    &.status-completed { color: #FF6B9D; }
  }
}

.card-items {
  padding: 16rpx 0;
}

.card-item {
  display: flex;
  align-items: center;
  padding: 10rpx 0;

  .item-name {
    flex: 1;
    font-size: 28rpx;
    color: #333333;
  }

  .item-quantity {
    font-size: 24rpx;
    color: #999999;
    margin: 0 16rpx;
  }

  .item-price {
    font-size: 26rpx;
    color: #333333;
    font-weight: 500;
  }
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 16rpx;
  border-top: 1rpx solid #FFF0F3;

  .order-time {
    font-size: 22rpx;
    color: #bbbbbb;
  }

  .card-footer-right {
    display: flex;
    align-items: baseline;

    .order-total {
      font-size: 24rpx;
      color: #666666;
    }

    .order-total-price {
      font-size: 32rpx;
      color: #FF4757;
      font-weight: 700;
    }
  }
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  gap: 16rpx;
  margin-top: 20rpx;
  padding-top: 16rpx;
  border-top: 1rpx dashed #FFE4EC;
}

.action-btn {
  padding: 12rpx 28rpx;
  border-radius: 30rpx;
  font-size: 24rpx;

  &:active {
    transform: scale(0.95);
  }
}

.review-btn {
  background: #FFF0F3;
  color: #FF6B9D;
  border: 1rpx solid #FFE4EC;
}

.reorder-btn {
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  color: #ffffff;
}

/* 加载更多 */
.load-more {
  text-align: center;
  padding: 32rpx 0;

  .load-more-text {
    font-size: 24rpx;
    color: #cccccc;
  }
}
</style>
