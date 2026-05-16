<template>
  <!-- 登录页 -->
  <view class="page-login">
    <!-- 可爱背景 -->
    <view class="login-bg">
      <!-- 渐变背景 -->
      <view class="bg-gradient"></view>
      <!-- 装饰爱心 -->
      <view class="bg-hearts">
        <text
          v-for="i in 12"
          :key="i"
          class="bg-heart"
          :style="{
            left: heartPositions[(i - 1) * 2] + '%',
            top: heartPositions[(i - 1) * 2 + 1] + '%',
            animationDelay: (i * 0.5) + 's',
            fontSize: (20 + Math.random() * 20) + 'rpx'
          }"
        >{{ heartEmojis[i % heartEmojis.length] }}</text>
      </view>
    </view>

    <!-- 登录内容 -->
    <view class="login-content">
      <!-- Logo区域 -->
      <view class="logo-section">
        <text class="logo-emoji">💕</text>
        <text class="logo-title">爱心点单</text>
        <text class="logo-subtitle">用爱为你点每一份餐</text>
      </view>

      <!-- 角色选择 -->
      <view class="role-section">
        <text class="role-title">选择你的身份</text>
        <view class="role-options">
          <view
            class="role-card"
            :class="{ active: selectedRole === 'boyfriend' }"
            @click="selectedRole = 'boyfriend'"
          >
            <text class="role-emoji">🤵</text>
            <text class="role-name">男朋友</text>
            <text class="role-desc">为她点一份爱心餐</text>
            <view v-if="selectedRole === 'boyfriend'" class="role-check">✓</view>
          </view>
          <view
            class="role-card"
            :class="{ active: selectedRole === 'girlfriend' }"
            @click="selectedRole = 'girlfriend'"
          >
            <text class="role-emoji">👰</text>
            <text class="role-name">女朋友</text>
            <text class="role-desc">接受他的爱心投喂</text>
            <view v-if="selectedRole === 'girlfriend'" class="role-check">✓</view>
          </view>
        </view>
      </view>

      <!-- 登录按钮 -->
      <view class="login-actions">
        <!-- 微信一键登录 -->
        <view class="login-btn wechat-btn" @click="handleWxLogin">
          <text class="btn-icon">💚</text>
          <text class="btn-text">微信一键登录</text>
        </view>

        <!-- 手机号登录 -->
        <view class="login-btn phone-btn" @click="handlePhoneLogin">
          <text class="btn-icon">📱</text>
          <text class="btn-text">手机号登录</text>
        </view>
      </view>

      <!-- 协议 -->
      <view class="agreement-section">
        <view class="agreement-check" @click="agreed = !agreed">
          <view class="check-box" :class="{ checked: agreed }">
            <text v-if="agreed" class="check-icon">✓</text>
          </view>
        </view>
        <text class="agreement-text">
          登录即代表同意
          <text class="agreement-link" @click="showAgreement('user')">《用户协议》</text>
          和
          <text class="agreement-link" @click="showAgreement('privacy')">《隐私政策》</text>
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 登录页
 * 可爱背景、角色选择、微信登录、手机号登录
 */
import { ref } from 'vue'
import { useUserStore } from '../../store/user.js'
import { wxLogin } from '../../utils/auth.js'

const userStore = useUserStore()

// 选中的角色
const selectedRole = ref('boyfriend')
// 是否同意协议
const agreed = ref(false)

// 背景爱心位置（预生成）
const heartPositions = [
  5, 8, 15, 15, 25, 5, 35, 20, 45, 10, 55, 25,
  65, 8, 75, 18, 85, 5, 90, 22, 10, 35, 20, 45
]

// 爱心emoji
const heartEmojis = ['💕', '💗', '💖', '❤️', '💝', '🩷']

/**
 * 微信一键登录
 */
async function handleWxLogin() {
  if (!agreed.value) {
    uni.showToast({ title: '请先同意用户协议', icon: 'none' })
    return
  }

  try {
    uni.showLoading({ title: '登录中...', mask: true })

    const res = await wxLogin(selectedRole.value)

    // 更新用户状态
    userStore.setUserData({
      ...res.userInfo,
      role: selectedRole.value
    })

    uni.hideLoading()

    uni.showToast({
      title: `登录成功！欢迎你${selectedRole.value === 'boyfriend' ? '，暖男' : '，甜心'} 💕`,
      icon: 'success',
      duration: 2000
    })

    // 跳转到首页
    setTimeout(() => {
      uni.switchTab({
        url: '/pages/index/index'
      })
    }, 1500)
  } catch (error) {
    uni.hideLoading()
    uni.showToast({
      title: '登录失败，请重试',
      icon: 'none'
    })
  }
}

/**
 * 手机号登录
 */
function handlePhoneLogin() {
  if (!agreed.value) {
    uni.showToast({ title: '请先同意用户协议', icon: 'none' })
    return
  }

  uni.showToast({
    title: '手机号登录功能开发中~',
    icon: 'none'
  })
}

/**
 * 显示协议
 */
function showAgreement(type) {
  const title = type === 'user' ? '用户协议' : '隐私政策'
  uni.showModal({
    title,
    content: '这里是协议内容，实际项目中请替换为真实协议文本。',
    showCancel: false,
    confirmText: '我知道了',
    confirmColor: '#FF6B9D'
  })
}
</script>

<style lang="scss" scoped>
.page-login {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

/* 可爱背景 */
.login-bg {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(180deg, #FFE4EC 0%, #FFF5F7 40%, #ffffff 100%);
}

.bg-hearts {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.bg-heart {
  position: absolute;
  opacity: 0.15;
  animation: heartFall 6s infinite;
}

/* 登录内容 */
.login-content {
  position: relative;
  z-index: 1;
  padding: 0 48rpx;
  padding-top: 180rpx;
}

/* Logo区域 */
.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 80rpx;
  animation: slideInUp 0.8s ease;

  .logo-emoji {
    font-size: 96rpx;
    margin-bottom: 16rpx;
    animation: heartbeat 1.5s infinite;
  }

  .logo-title {
    font-size: 52rpx;
    font-weight: 800;
    background: linear-gradient(135deg, #FF6B9D, #FFA07A);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    margin-bottom: 8rpx;
  }

  .logo-subtitle {
    font-size: 28rpx;
    color: #999999;
  }
}

/* 角色选择 */
.role-section {
  margin-bottom: 60rpx;
  animation: slideInUp 0.8s ease 0.2s both;
}

.role-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  display: block;
  text-align: center;
  margin-bottom: 28rpx;
}

.role-options {
  display: flex;
  gap: 24rpx;
}

.role-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 36rpx 20rpx;
  background: #ffffff;
  border-radius: 24rpx;
  border: 3rpx solid transparent;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.08);
  position: relative;
  transition: all 0.3s ease;

  &.active {
    border-color: #FF6B9D;
    background: #FFF5F7;
    box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.2);
    transform: translateY(-4rpx);

    .role-emoji {
      transform: scale(1.15);
    }
  }

  &:active {
    transform: scale(0.97);
  }
}

.role-emoji {
  font-size: 72rpx;
  margin-bottom: 16rpx;
  transition: transform 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.role-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 8rpx;
}

.role-desc {
  font-size: 22rpx;
  color: #999999;
}

.role-check {
  position: absolute;
  top: 12rpx;
  right: 12rpx;
  width: 40rpx;
  height: 40rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  text {
    font-size: 22rpx;
    color: #ffffff;
    font-weight: 700;
  }
}

/* 登录按钮 */
.login-actions {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  margin-bottom: 40rpx;
  animation: slideInUp 0.8s ease 0.4s both;
}

.login-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12rpx;
  padding: 28rpx;
  border-radius: 50rpx;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.97);
  }

  .btn-icon {
    font-size: 32rpx;
  }

  .btn-text {
    font-size: 30rpx;
    font-weight: 600;
  }
}

.wechat-btn {
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  box-shadow: 0 6rpx 24rpx rgba(255, 107, 157, 0.4);

  .btn-text {
    color: #ffffff;
  }
}

.phone-btn {
  background: #ffffff;
  border: 2rpx solid #FFE4EC;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.08);

  .btn-text {
    color: #FF6B9D;
  }
}

/* 协议 */
.agreement-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  animation: fadeIn 1s ease 0.6s both;
}

.agreement-check {
  .check-box {
    width: 32rpx;
    height: 32rpx;
    border: 2rpx solid #ddd;
    border-radius: 6rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;

    &.checked {
      background: linear-gradient(135deg, #FF6B9D, #FFA07A);
      border-color: #FF6B9D;

      .check-icon {
        font-size: 20rpx;
        color: #ffffff;
        font-weight: 700;
      }
    }
  }
}

.agreement-text {
  font-size: 22rpx;
  color: #999999;

  .agreement-link {
    color: #FF6B9D;
  }
}
</style>
