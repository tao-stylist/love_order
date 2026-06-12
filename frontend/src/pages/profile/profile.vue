<template>
  <!-- 个人中心页 -->
  <view class="page-profile">
    <!-- 自定义导航栏 -->
    <view class="profile-header" :style="{ paddingTop: statusBarHeight + 'px' }">
      <!-- 背景装饰 -->
      <view class="header-bg">
        <text class="deco-heart deco-1">💕</text>
        <text class="deco-heart deco-2">💗</text>
        <text class="deco-heart deco-3">💖</text>
      </view>

      <!-- 用户信息 -->
      <view class="user-info-section">
        <view class="user-avatar" @click="handleAvatarClick">
          <view class="avatar-circle">
            <text class="avatar-emoji">{{ userStore.roleEmoji || '😊' }}</text>
          </view>
          <view class="avatar-badge">
            <text>{{ userStore.roleText || '未设置' }}</text>
          </view>
        </view>
        <view class="user-detail">
          <text class="user-nickname">{{ userStore.userInfo.nickname || '爱心用户' }}</text>
          <text class="user-id">ID: {{ userStore.userInfo.id || '10086' }}</text>
        </view>
        <view class="settings-btn" @click="goSettings">
          <text>⚙️</text>
        </view>
      </view>

      <!-- 爱心积分展示 -->
      <view class="points-card">
        <view class="points-left">
          <text class="points-label">我的爱心积分</text>
          <view class="points-value-row">
            <text class="points-number">{{ userStore.lovePoints }}</text>
            <text class="points-heart">💕</text>
          </view>
        </view>
        <view class="sign-btn" :class="{ signed: userStore.isSigned }" @click="handleSign">
          <text class="sign-btn-text">{{ userStore.isSigned ? '已签到 ✅' : '每日签到 💕' }}</text>
        </view>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-section">
      <!-- 绑定另一半 -->
      <view class="menu-item" @click="goBind">
        <view class="menu-item-left">
          <text class="menu-icon">💑</text>
          <text class="menu-text">绑定另一半</text>
        </view>
        <view class="menu-item-right">
          <text v-if="isBinded" class="menu-extra">已绑定</text>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <!-- 纪念日管理 -->
      <view class="menu-item" @click="goAnniversary">
        <view class="menu-item-left">
          <text class="menu-icon">🎉</text>
          <text class="menu-text">纪念日管理</text>
        </view>
        <view class="menu-item-right">
          <text v-if="nextAnniversary" class="menu-extra">{{ nextAnniversary }}</text>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <!-- 我的评价 -->
      <view class="menu-item" @click="goMyReviews">
        <view class="menu-item-left">
          <text class="menu-icon">💬</text>
          <text class="menu-text">我的评价</text>
        </view>
        <view class="menu-item-right">
          <text class="menu-arrow">></text>
        </view>
      </view>

      <!-- 收货地址 -->
      <view class="menu-item" @click="goAddress">
        <view class="menu-item-left">
          <text class="menu-icon">📍</text>
          <text class="menu-text">收货地址</text>
        </view>
        <view class="menu-item-right">
          <text class="menu-arrow">></text>
        </view>
      </view>

      <!-- 积分规则 -->
      <view class="menu-item" @click="showPointsRule">
        <view class="menu-item-left">
          <text class="menu-icon">💝</text>
          <text class="menu-text">积分规则</text>
        </view>
        <view class="menu-item-right">
          <text class="menu-arrow">></text>
        </view>
      </view>

      <!-- 关于我们 -->
      <view class="menu-item" @click="showAbout">
        <view class="menu-item-left">
          <text class="menu-icon">💕</text>
          <text class="menu-text">关于爱心点单</text>
        </view>
        <view class="menu-item-right">
          <text class="menu-extra">v1.0.0</text>
          <text class="menu-arrow">></text>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <view class="logout-btn" @click="handleLogout">
        <text class="logout-text">退出登录</text>
      </view>
    </view>

    <!-- 底部占位 -->
    <view style="height: 60rpx;"></view>
  </view>
</template>

<script setup>
/**
 * 个人中心页
 * 用户信息、爱心积分、每日签到、功能菜单
 */
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../../store/user.js'
import { getAnniversaryCountdown } from '../../utils/date.js'
import { getUserInfo } from '../../utils/auth.js'

const userStore = useUserStore()

// 状态栏高度
const statusBarHeight = ref(0)

// 是否已绑定
const isBinded = computed(() => {
  const userInfo = getUserInfo()
  return userInfo && userInfo.partnerId
})

// 下一个纪念日倒计时
const nextAnniversary = computed(() => {
  if (userStore.anniversaries.length === 0) return ''
  const next = userStore.anniversaries[0]
  const countdown = getAnniversaryCountdown(next.date)
  return `${next.name} 还有${countdown.days}天`
})

onMounted(() => {
  const sysInfo = uni.getSystemInfoSync()
  statusBarHeight.value = sysInfo.statusBarHeight || 20
})

/**
 * 点击头像
 */
function handleAvatarClick() {
  uni.showToast({
    title: '头像修改功能开发中~',
    icon: 'none'
  })
}

/**
 * 每日签到
 */
function handleSign() {
  userStore.dailySign()
}

/**
 * 跳转到绑定页面
 */
function goBind() {
  uni.navigateTo({
    url: '/pages/bind/bind'
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
 * 跳转到我的评价
 */
function goMyReviews() {
  uni.showToast({
    title: '评价功能开发中~',
    icon: 'none'
  })
}

/**
 * 跳转到收货地址
 */
function goAddress() {
  uni.showToast({
    title: '地址管理开发中~',
    icon: 'none'
  })
}

/**
 * 显示积分规则
 */
function showPointsRule() {
  uni.showModal({
    title: '💝 积分规则',
    content: '1. 每日签到可获得5-15积分\n2. 每完成一笔订单可获得10积分\n3. 每发表一条评价可获得5积分\n4. 100积分可抵扣1元\n5. 积分永不过期',
    showCancel: false,
    confirmText: '我知道了',
    confirmColor: '#FF6B9D'
  })
}

/**
 * 显示关于
 */
function showAbout() {
  uni.showModal({
    title: '💕 关于爱心点单',
    content: '爱心点单 v1.0.0\n\n用爱为你点每一份餐\n让每一顿饭都充满甜蜜\n\nMade with ❤️',
    showCancel: false,
    confirmText: '好的',
    confirmColor: '#FF6B9D'
  })
}

/**
 * 跳转到设置
 */
function goSettings() {
  uni.showToast({
    title: '设置功能开发中~',
    icon: 'none'
  })
}

/**
 * 退出登录
 */
function handleLogout() {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    confirmColor: '#FF6B9D',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.page-profile {
  min-height: 100vh;
  background: #FFF5F7;
}

/* 头部区域 */
.profile-header {
  position: relative;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  padding: 0 32rpx 48rpx;
  border-radius: 0 0 40rpx 40rpx;
  overflow: hidden;
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.deco-heart {
  position: absolute;
  font-size: 28rpx;
  opacity: 0.2;
  animation: heartFall 5s infinite;

  &.deco-1 { top: 10%; left: 15%; animation-delay: 0s; }
  &.deco-2 { top: 30%; right: 20%; animation-delay: 1.5s; }
  &.deco-3 { top: 50%; left: 60%; animation-delay: 3s; }
}

/* 用户信息 */
.user-info-section {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  padding: 32rpx 0;
}

.user-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
}

.avatar-circle {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  border: 4rpx solid rgba(255, 255, 255, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;

  .avatar-emoji {
    font-size: 60rpx;
  }
}

.avatar-badge {
  background: rgba(255, 255, 255, 0.25);
  padding: 2rpx 16rpx;
  border-radius: 20rpx;

  text {
    font-size: 20rpx;
    color: #ffffff;
  }
}

.user-detail {
  flex: 1;
  margin-left: 24rpx;

  .user-nickname {
    font-size: 36rpx;
    font-weight: 700;
    color: #ffffff;
    display: block;
  }

  .user-id {
    font-size: 22rpx;
    color: rgba(255, 255, 255, 0.7);
    margin-top: 4rpx;
    display: block;
  }
}

.settings-btn {
  width: 64rpx;
  height: 64rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  text {
    font-size: 32rpx;
  }

  &:active {
    background: rgba(255, 255, 255, 0.3);
  }
}

/* 积分卡片 */
.points-card {
  position: relative;
  z-index: 2;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 20rpx;
  padding: 28rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  backdrop-filter: blur(10rpx);
}

.points-left {
  .points-label {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
    display: block;
  }

  .points-value-row {
    display: flex;
    align-items: baseline;
    gap: 8rpx;
    margin-top: 8rpx;

    .points-number {
      font-size: 56rpx;
      font-weight: 800;
      color: #ffffff;
      line-height: 1;
    }

    .points-heart {
      font-size: 32rpx;
      animation: heartbeat 1.5s infinite;
    }
  }
}

.sign-btn {
  padding: 16rpx 32rpx;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 40rpx;

  &.signed {
    background: rgba(255, 255, 255, 0.4);
  }

  &:active {
    transform: scale(0.95);
  }

  .sign-btn-text {
    font-size: 24rpx;
    color: #FF6B9D;
    font-weight: 600;
  }
}

/* 功能菜单 */
.menu-section {
  margin: 24rpx 24rpx 0;
  background: #ffffff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 28rpx;
  border-bottom: 1rpx solid #FFF0F3;

  &:last-child {
    border-bottom: none;
  }

  &:active {
    background: #FFF5F7;
  }
}

.menu-item-left {
  display: flex;
  align-items: center;
  gap: 16rpx;

  .menu-icon {
    font-size: 36rpx;
  }

  .menu-text {
    font-size: 28rpx;
    color: #333333;
  }
}

.menu-item-right {
  display: flex;
  align-items: center;
  gap: 12rpx;

  .menu-extra {
    font-size: 24rpx;
    color: #999999;
  }

  .menu-arrow {
    font-size: 28rpx;
    color: #cccccc;
  }
}

/* 退出登录 */
.logout-section {
  margin: 48rpx 24rpx 0;
}

.logout-btn {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  text-align: center;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);

  &:active {
    background: #FFF5F7;
  }

  .logout-text {
    font-size: 28rpx;
    color: #FF4757;
  }
}
</style>
