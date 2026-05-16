<template>
  <!-- 首页 - 爱心点单 -->
  <view class="page-index">
    <!-- 自定义导航栏 -->
    <view class="custom-navbar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <view class="navbar-content">
        <text class="navbar-title">💕 爱心点单</text>
        <view class="navbar-right">
          <text class="notification-icon" @click="goOrders">🔔</text>
        </view>
      </view>
    </view>

    <!-- 可滚动内容区域 -->
    <scroll-view scroll-y class="page-content" :style="{ paddingTop: navbarHeight + 'px' }">
      <!-- 情侣头像 + 纪念日倒计时 -->
      <view class="couple-banner">
        <view class="banner-bg">
          <!-- 装饰爱心 -->
          <text class="deco-heart deco-1">💗</text>
          <text class="deco-heart deco-2">💕</text>
          <text class="deco-heart deco-3">💖</text>
          <text class="deco-heart deco-4">❤️</text>
        </view>
        <view class="couple-info">
          <!-- 情侣头像 -->
          <view class="couple-avatars">
            <view class="avatar-wrapper">
              <view class="avatar avatar-left">
                <text class="avatar-emoji">🤵</text>
              </view>
              <text class="avatar-label">他</text>
            </view>
            <view class="love-center">
              <text class="love-icon">💕</text>
            </view>
            <view class="avatar-wrapper">
              <view class="avatar avatar-right">
                <text class="avatar-emoji">👰</text>
              </view>
              <text class="avatar-label">她</text>
            </view>
          </view>
          <!-- 纪念日倒计时 -->
          <view class="anniversary-info">
            <Countdown
              v-if="userStore.loveStartDate"
              :target-date="userStore.loveStartDate"
              type="together"
            />
            <view v-else class="set-date-hint" @click="goAnniversary">
              <text class="hint-text">点击设置在一起的日子 💕</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 每日推荐区域 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">🌟 今日推荐</text>
          <text class="section-more" @click="goMenu">查看全部 ></text>
        </view>
        <scroll-view scroll-x class="recommend-scroll" :show-scrollbar="false">
          <view class="recommend-list">
            <view
              v-for="item in recommendList"
              :key="item.id"
              class="recommend-card"
              @click="goMenu"
            >
              <view class="recommend-image" :style="{ background: getCardBg(item.id) }">
                <text class="recommend-emoji">{{ getCategoryEmoji(item.category) }}</text>
              </view>
              <text class="recommend-name">{{ item.name }}</text>
              <view class="recommend-bottom">
                <text class="recommend-price">¥{{ item.price }}</text>
                <text class="recommend-sales">{{ item.sales }}人点过</text>
              </view>
            </view>
          </view>
        </scroll-view>
      </view>

      <!-- 心情点单入口 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">😊 心情点单</text>
          <text class="section-subtitle">选择你的心情，为你推荐美食</text>
        </view>
        <view class="mood-grid">
          <view
            v-for="mood in moods"
            :key="mood.key"
            class="mood-item"
            :style="{ background: mood.bg }"
            @click="handleMoodClick(mood)"
          >
            <text class="mood-emoji">{{ mood.emoji }}</text>
            <text class="mood-label">{{ mood.label }}</text>
          </view>
        </view>
      </view>

      <!-- 快捷下单区域 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">⚡ 快捷下单</text>
        </view>
        <view class="quick-actions">
          <view class="quick-item" @click="goMenu">
            <view class="quick-icon-wrapper" style="background: linear-gradient(135deg, #FFE4EC, #FFF0F3);">
              <text class="quick-icon">🍱</text>
            </view>
            <text class="quick-label">全部菜品</text>
          </view>
          <view class="quick-item" @click="goMenuWithCategory('set')">
            <view class="quick-icon-wrapper" style="background: linear-gradient(135deg, #FFF0E6, #FFF8F0);">
              <text class="quick-icon">💑</text>
            </view>
            <text class="quick-label">情侣套餐</text>
          </view>
          <view class="quick-item" @click="goMenuWithCategory('drink')">
            <view class="quick-icon-wrapper" style="background: linear-gradient(135deg, #E3F2FD, #F3F9FF);">
              <text class="quick-icon">🧋</text>
            </view>
            <text class="quick-label">奶茶饮品</text>
          </view>
          <view class="quick-item" @click="goMenuWithCategory('dessert')">
            <view class="quick-icon-wrapper" style="background: linear-gradient(135deg, #F3E5F5, #FCE4EC);">
              <text class="quick-icon">🍰</text>
            </view>
            <text class="quick-label">甜蜜甜品</text>
          </view>
        </view>
      </view>

      <!-- 爱心小贴士 -->
      <view class="section">
        <view class="tip-card">
          <text class="tip-icon">💡</text>
          <text class="tip-text">今日小贴士：点一份甜蜜双人餐，和TA一起享受美好时光~</text>
        </view>
      </view>

      <!-- 底部占位 -->
      <view style="height: 140rpx;"></view>
    </scroll-view>
  </view>
</template>

<script setup>
/**
 * 首页 - 爱心点单
 * 情侣头像、纪念日倒计时、每日推荐、心情点单、快捷下单
 */
import { ref, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/user.js'
import { useOrderStore } from '../../store/order.js'
import Countdown from '../../components/Countdown/Countdown.vue'

const userStore = useUserStore()
const orderStore = useOrderStore()

// 状态栏高度
const statusBarHeight = ref(0)
// 导航栏高度
const navbarHeight = ref(0)

// 心情列表
const moods = ref([
  { key: 'happy', emoji: '😊', label: '开心', bg: 'linear-gradient(135deg, #FFF9C4, #FFF59D)' },
  { key: 'sad', emoji: '😢', label: '难过', bg: 'linear-gradient(135deg, #E3F2FD, #BBDEFB)' },
  { key: 'angry', emoji: '😤', label: '生气', bg: 'linear-gradient(135deg, #FFEBEE, #FFCDD2)' },
  { key: 'miss', emoji: '🥺', label: '想念', bg: 'linear-gradient(135deg, #FCE4EC, #F8BBD0)' },
  { key: 'surprise', emoji: '🤩', label: '惊喜', bg: 'linear-gradient(135deg, #E8F5E9, #C8E6C9)' },
  { key: 'daily', emoji: '😌', label: '日常', bg: 'linear-gradient(135deg, #FFF3E0, #FFE0B2)' }
])

// 推荐菜品
const recommendList = computed(() => orderStore.recommendList)

onMounted(() => {
  // 获取系统信息计算状态栏高度
  const sysInfo = uni.getSystemInfoSync()
  statusBarHeight.value = sysInfo.statusBarHeight || 20
  navbarHeight.value = statusBarHeight.value + 44
})

/**
 * 跳转到菜单页
 */
function goMenu() {
  uni.switchTab({
    url: '/pages/menu/menu'
  })
}

/**
 * 带分类跳转到菜单页
 */
function goMenuWithCategory(category) {
  orderStore.currentCategory = category
  uni.switchTab({
    url: '/pages/menu/menu'
  })
}

/**
 * 跳转到订单页
 */
function goOrders() {
  uni.switchTab({
    url: '/pages/orders/orders'
  })
}

/**
 * 跳转到纪念日管理
 */
function goAnniversary() {
  uni.navigateTo({
    url: '/pages/anniversary/anniversary'
  })
}

/**
 * 心情点单点击
 */
function handleMoodClick(mood) {
  uni.showToast({
    title: `${mood.emoji} ${mood.label}模式已开启`,
    icon: 'none',
    duration: 1500
  })

  // 延迟跳转到菜单页
  setTimeout(() => {
    goMenu()
  }, 800)
}

/**
 * 获取卡片背景色
 */
function getCardBg(id) {
  const colors = [
    'linear-gradient(135deg, #FFE4EC, #FFF0F3)',
    'linear-gradient(135deg, #FFF0E6, #FFF8F0)',
    'linear-gradient(135deg, #E8F5E9, #F1F8E9)',
    'linear-gradient(135deg, #E3F2FD, #F3F9FF)',
    'linear-gradient(135deg, #F3E5F5, #FCE4EC)'
  ]
  return colors[id % colors.length]
}

/**
 * 获取分类emoji
 */
function getCategoryEmoji(category) {
  const emojiMap = {
    'recommend': '💝',
    'staple': '🍱',
    'snack': '🍟',
    'drink': '🧋',
    'dessert': '🍰',
    'soup': '🍲',
    'set': '💑'
  }
  return emojiMap[category] || '🍽️'
}
</script>

<style lang="scss" scoped>
.page-index {
  min-height: 100vh;
  background: #FFF5F7;
}

/* 自定义导航栏 */
.custom-navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
}

.navbar-content {
  height: 88rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx;

  .navbar-title {
    font-size: 36rpx;
    font-weight: 700;
    color: #ffffff;
  }

  .navbar-right {
    .notification-icon {
      font-size: 40rpx;
    }
  }
}

/* 页面内容 */
.page-content {
  height: 100vh;
}

/* 情侣头像横幅 */
.couple-banner {
  position: relative;
  margin: 16rpx 24rpx;
  border-radius: 24rpx;
  overflow: hidden;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  padding: 40rpx 24rpx;
}

.banner-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.deco-heart {
  position: absolute;
  font-size: 32rpx;
  opacity: 0.3;
  animation: heartFall 4s infinite;

  &.deco-1 { top: 10%; left: 10%; animation-delay: 0s; }
  &.deco-2 { top: 20%; right: 15%; animation-delay: 1s; }
  &.deco-3 { bottom: 20%; left: 20%; animation-delay: 2s; }
  &.deco-4 { bottom: 10%; right: 10%; animation-delay: 3s; }
}

.couple-info {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.couple-avatars {
  display: flex;
  align-items: center;
  gap: 24rpx;
  margin-bottom: 24rpx;
}

.avatar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3rpx solid rgba(255, 255, 255, 0.5);

  .avatar-emoji {
    font-size: 52rpx;
  }
}

.avatar-label {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
}

.love-center {
  .love-icon {
    font-size: 48rpx;
    animation: heartbeat 1.5s infinite;
  }
}

.anniversary-info {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20rpx;
  padding: 16rpx 32rpx;
}

.set-date-hint {
  padding: 8rpx 0;

  .hint-text {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.9);
  }
}

/* 通用section */
.section {
  margin: 24rpx 24rpx 0;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: 700;
    color: #333333;
  }

  .section-more {
    font-size: 24rpx;
    color: #FF6B9D;
  }

  .section-subtitle {
    font-size: 22rpx;
    color: #999999;
  }
}

/* 每日推荐 */
.recommend-scroll {
  white-space: nowrap;
}

.recommend-list {
  display: inline-flex;
  gap: 20rpx;
  padding: 4rpx 0;
}

.recommend-card {
  width: 240rpx;
  background: #ffffff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 157, 0.08);
  animation: fadeIn 0.5s ease;

  &:active {
    transform: scale(0.97);
  }
}

.recommend-image {
  width: 100%;
  height: 180rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  .recommend-emoji {
    font-size: 72rpx;
  }
}

.recommend-name {
  display: block;
  font-size: 26rpx;
  font-weight: 600;
  color: #333333;
  padding: 16rpx 16rpx 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.recommend-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16rpx 16rpx;

  .recommend-price {
    font-size: 28rpx;
    color: #FF4757;
    font-weight: 700;
  }

  .recommend-sales {
    font-size: 20rpx;
    color: #bbbbbb;
  }
}

/* 心情点单 */
.mood-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16rpx;
}

.mood-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 28rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
  animation: fadeIn 0.5s ease;

  &:active {
    transform: scale(0.95);
  }

  .mood-emoji {
    font-size: 56rpx;
    margin-bottom: 8rpx;
  }

  .mood-label {
    font-size: 24rpx;
    color: #666666;
    font-weight: 500;
  }
}

/* 快捷下单 */
.quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;

  &:active {
    transform: scale(0.95);
  }
}

.quick-icon-wrapper {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  .quick-icon {
    font-size: 48rpx;
  }
}

.quick-label {
  font-size: 22rpx;
  color: #666666;
}

/* 爱心小贴士 */
.tip-card {
  display: flex;
  align-items: center;
  gap: 16rpx;
  background: linear-gradient(135deg, #FFF9E6, #FFF3CC);
  border-radius: 16rpx;
  padding: 24rpx;
  border: 1rpx solid #FFE4B5;

  .tip-icon {
    font-size: 36rpx;
    flex-shrink: 0;
  }

  .tip-text {
    font-size: 24rpx;
    color: #8B6914;
    line-height: 1.6;
  }
}
</style>
