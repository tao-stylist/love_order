<template>
  <!-- 首页 - 爱心点单主页面 -->
  <view class="page-index">
    <!-- 状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>

    <!-- 顶部区域 -->
    <view class="header-section">
      <!-- 问候语 -->
      <view class="greeting-area">
        <view class="greeting-left">
          <text class="greeting-text">{{ greeting }}</text>
          <text class="user-name">{{ userStore.userInfo?.nickname || '小可爱' }} 💕</text>
        </view>
        <view class="couple-badge" v-if="userStore.loveStartDate">
          <text class="badge-icon">💑</text>
          <text class="badge-text">{{ coupleDays }}天</text>
        </view>
      </view>

      <!-- 搜索栏 -->
      <view class="search-bar" @click="goToMenu">
        <text class="search-icon">🔍</text>
        <text class="search-placeholder">搜索你想要的美食...</text>
      </view>
    </view>

    <!-- 主体内容 -->
    <scroll-view scroll-y class="main-content" :show-scrollbar="false">
      <!-- 今日推荐 -->
      <view class="section recommend-section">
        <view class="section-header">
          <text class="section-title">今日推荐 ✨</text>
          <text class="section-more" @click="goToMenu">查看全部 ></text>
        </view>
        <view class="recommend-cards">
          <view
            class="recommend-card"
            v-for="item in recommendItems"
            :key="item.id"
            @click="goToMenuItem(item)"
          >
            <image class="card-image" :src="item.image" mode="aspectFill" />
            <view class="card-info">
              <text class="card-name">{{ item.name }}</text>
              <text class="card-desc">{{ item.desc }}</text>
              <view class="card-bottom">
                <text class="card-price">¥{{ item.price }}</text>
                <view class="add-btn" @click.stop="addToCart(item)">
                  <text>+</text>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 快捷入口 -->
      <view class="section quick-entry-section">
        <view class="section-header">
          <text class="section-title">快捷入口 🚀</text>
        </view>
        <view class="quick-entries">
          <view class="quick-item" @click="goToMenu">
            <view class="quick-icon" style="background: linear-gradient(135deg, #FF6B9D, #FFA07A);">
              <text>🍽️</text>
            </view>
            <text class="quick-text">点餐</text>
          </view>
          <view class="quick-item" @click="goToOrders">
            <view class="quick-icon" style="background: linear-gradient(135deg, #FFA07A, #FFD700);">
              <text>📋</text>
            </view>
            <text class="quick-text">订单</text>
          </view>
          <view class="quick-item" @click="goToAnniversary">
            <view class="quick-icon" style="background: linear-gradient(135deg, #FF6B9D, #FF4757);">
              <text>💝</text>
            </view>
            <text class="quick-text">纪念日</text>
          </view>
          <view class="quick-item" @click="goToProfile">
            <view class="quick-icon" style="background: linear-gradient(135deg, #FF8FB5, #FF6B9D);">
              <text>👤</text>
            </view>
            <text class="quick-text">我的</text>
          </view>
        </view>
      </view>

      <!-- 甜蜜提示 -->
      <view class="section tips-section">
        <view class="sweet-tip">
          <text class="tip-icon">💌</text>
          <text class="tip-text">{{ dailyTip }}</text>
        </view>
      </view>

      <!-- 底部占位 -->
      <view style="height: 120rpx;"></view>
    </scroll-view>

    <!-- 购物车浮动按钮 -->
    <CartFloat />

    <!-- 爱心动画 -->
    <view class="floating-hearts">
      <view
        class="heart"
        v-for="i in 5"
        :key="i"
        :style="{
          left: (i * 18) + '%',
          animationDelay: (i * 0.3) + 's'
        }"
      >❤️</view>
    </view>
  </view>
</template>

<script setup>
/**
 * 首页 - 爱心点单主页面
 * 包含问候语、今日推荐、快捷入口、甜蜜提示
 */
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../../store/user.js'
import { useOrderStore } from '../../store/order.js'
import CartFloat from '../../components/CartFloat/CartFloat.vue'

const userStore = useUserStore()
const orderStore = useOrderStore()

// 状态栏高度
const statusBarHeight = ref(0)

// 问候语
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '夜深了～'
  if (hour < 9) return '早上好☀️'
  if (hour < 12) return '上午好～'
  if (hour < 14) return '中午好🍽️'
  if (hour < 18) return '下午好～'
  if (hour < 22) return '晚上好🌙'
  return '夜深了～'
})

// 情侣在一起天数
const coupleDays = computed(() => {
  if (!userStore.loveStartDate) return 0
  const start = new Date(userStore.loveStartDate)
  const now = new Date()
  const diff = now.getTime() - start.getTime()
  return Math.floor(diff / (1000 * 60 * 60 * 24))
})

// 每日甜蜜提示
const dailyTips = [
  '记得按时吃饭哦～爱你❤️',
  '今天也要元气满满呀💪',
  '想你了，随时来点餐吧～',
  '有你的日子，每天都是甜蜜的',
  '一起吃好吃的，创造美好回忆 🍰',
  '爱是和你一起分享每一顿饭 💕',
  '今天想吃什么？我陪你～',
  '有你在身边，连空气都是甜的 ✨'
]

const dailyTip = computed(() => {
  const dayOfYear = Math.floor((new Date() - new Date(new Date().getFullYear(), 0, 0)) / (1000 * 60 * 60 * 24))
  return dailyTips[dayOfYear % dailyTips.length]
})

// 推荐菜品
const recommendItems = computed(() => {
  return orderStore.menuList.slice(0, 3)
})

onMounted(() => {
  // 获取状态栏高度
  const systemInfo = uni.getSystemInfoSync()
  statusBarHeight.value = systemInfo.statusBarHeight || 20

  // 检查登录状态
  userStore.checkLogin()
})

function goToMenu() {
  uni.switchTab({
    url: '/pages/menu/menu'
  })
}

function goToMenuItem(item) {
  uni.showToast({
    title: `查看 ${item.name}`,
    icon: 'none'
  })
}

function goToOrders() {
  uni.switchTab({
    url: '/pages/orders/orders'
  })
}

function goToAnniversary() {
  uni.navigateTo({
    url: '/pages/anniversary/anniversary'
  })
}

function goToProfile() {
  uni.switchTab({
    url: '/pages/profile/profile'
  })
}

function addToCart(item) {
  orderStore.addToCart(item)
  uni.showToast({
    title: '已加入购物车 🛒',
    icon: 'none'
  })
}
</script>

<style lang="scss" scoped>
.page-index {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #FFF5F7 0%, #FFF0F3 100%);
  position: relative;
  overflow: hidden;
}

.status-bar {
  background: transparent;
}

/* 头部区域 */
.header-section {
  padding: 0 30rpx 30rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 0 0 40rpx 40rpx;
}

.greeting-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
  padding-top: 10rpx;
}

.greeting-left {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.greeting-text {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
}

.user-name {
  font-size: 40rpx;
  font-weight: 700;
  color: #ffffff;
}

.couple-badge {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10px);
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
  gap: 8rpx;

  .badge-icon {
    font-size: 32rpx;
  }

  .badge-text {
    font-size: 24rpx;
    color: #ffffff;
    font-weight: 600;
  }
}

.search-bar {
  display: flex;
  align-items: center;
  background: #ffffff;
  padding: 24rpx 30rpx;
  border-radius: 40rpx;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.15);

  .search-icon {
    font-size: 32rpx;
    margin-right: 16rpx;
  }

  .search-placeholder {
    font-size: 28rpx;
    color: #999999;
  }
}

/* 主体内容 */
.main-content {
  flex: 1;
  padding: 0 24rpx;
  margin-top: -20rpx;
}

.section {
  margin-bottom: 40rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 34rpx;
  font-weight: 700;
  color: #333333;
}

.section-more {
  font-size: 24rpx;
  color: #FF6B9D;
}

/* 推荐卡片 */
.recommend-cards {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.recommend-card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 20rpx;
  display: flex;
  gap: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.1);
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.98);
    box-shadow: 0 2rpx 10rpx rgba(255, 107, 157, 0.15);
  }
}

.card-image {
  width: 180rpx;
  height: 180rpx;
  border-radius: 16rpx;
  background: linear-gradient(135deg, #FFF0F3, #FFE4EC);
}

.card-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 8rpx 0;
}

.card-name {
  font-size: 32rpx;
  font-weight: 700;
  color: #333333;
  margin-bottom: 8rpx;
}

.card-desc {
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 12rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-price {
  font-size: 36rpx;
  font-weight: 700;
  color: #FF6B9D;
}

.add-btn {
  width: 56rpx;
  height: 56rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 36rpx;
  font-weight: 700;
  box-shadow: 0 4rpx 12rpx rgba(255, 107, 157, 0.3);

  &:active {
    transform: scale(0.9);
  }
}

/* 快捷入口 */
.quick-entries {
  display: flex;
  justify-content: space-around;
  background: #ffffff;
  border-radius: 24rpx;
  padding: 30rpx 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.1);
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;

  &:active {
    opacity: 0.8;
    transform: scale(0.95);
  }
}

.quick-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 157, 0.2);
}

.quick-text {
  font-size: 24rpx;
  color: #666666;
  font-weight: 500;
}

/* 甜蜜提示 */
.tips-section {
  margin-bottom: 60rpx;
}

.sweet-tip {
  background: linear-gradient(135deg, rgba(255, 107, 157, 0.1), rgba(255, 160, 122, 0.1));
  border: 2rpx solid rgba(255, 107, 157, 0.2);
  border-radius: 20rpx;
  padding: 24rpx 30rpx;
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.tip-icon {
  font-size: 40rpx;
  flex-shrink: 0;
}

.tip-text {
  font-size: 26rpx;
  color: #FF6B9D;
  line-height: 1.6;
}

/* 浮动爱心 */
.floating-hearts {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 400rpx;
  pointer-events: none;
  overflow: hidden;
  z-index: 0;
}

.heart {
  position: absolute;
  font-size: 24rpx;
  opacity: 0.3;
  animation: floatUp 4s ease-in-out infinite;
}

@keyframes floatUp {
  0% {
    transform: translateY(400rpx) scale(0.5);
    opacity: 0;
  }
  20% {
    opacity: 0.3;
  }
  80% {
    opacity: 0.3;
  }
  100% {
    transform: translateY(-100rpx) scale(1.2);
    opacity: 0;
  }
}
</style>
