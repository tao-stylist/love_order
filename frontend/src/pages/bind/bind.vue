<template>
  <!-- 绑定页面 -->
  <view class="page-bind">
    <!-- 头部装饰 -->
    <view class="bind-header">
      <text class="header-emoji">💕</text>
      <text class="header-title">绑定另一半</text>
      <text class="header-desc">绑定成功后，你们可以一起点餐啦</text>
    </view>

    <!-- 我的绑定码 -->
    <view class="bind-code-section">
      <view class="section-title">我的绑定码</view>
      <view class="bind-code-card">
        <text class="bind-code" v-if="bindCode">{{ bindCode }}</text>
        <text class="bind-code-empty" v-else>生成中...</text>
        <view class="copy-btn" @click="copyBindCode">
          <text class="copy-icon">📋</text>
          <text class="copy-text">{{ copyText }}</text>
        </view>
      </view>
      <text class="bind-tip">把绑定码发给你的另一半，让他/她输入绑定</text>
    </view>

    <!-- 输入绑定码 -->
    <view class="bind-input-section">
      <view class="section-title">绑定另一半</view>
      <view class="bind-input-card">
        <input
          class="bind-input"
          type="text"
          v-model="inputBindCode"
          placeholder="请输入对方的绑定码"
          maxlength="6"
          @input="onInputChange"
        />
        <view
          class="bind-btn"
          :class="{ disabled: !canBind }"
          @click="handleBind"
        >
          绑定
        </view>
      </view>
    </view>

    <!-- 绑定状态 -->
    <view class="bind-status-section" v-if="hasPartner">
      <view class="section-title">我们的绑定</view>
      <view class="bind-status-card">
        <view class="status-icon">🎉</view>
        <text class="status-text">已成功绑定！</text>
        <view class="partner-info" v-if="partnerInfo">
          <text class="partner-name">{{ partnerInfo.nickname || '另一半' }}</text>
        </view>
      </view>
    </view>

    <!-- 说明卡片 -->
    <view class="help-section">
      <view class="help-card">
        <text class="help-title">📌 说明</text>
        <view class="help-item">
          <text class="help-dot">•</text>
          <text class="help-text">每个用户只能绑定一个人</text>
        </view>
        <view class="help-item">
          <text class="help-dot">•</text>
          <text class="help-text">绑定后双方可以看到共同的订单</text>
        </view>
        <view class="help-item">
          <text class="help-dot">•</text>
          <text class="help-text">解绑请联系客服</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 绑定页面
 * 用户可以在这里查看自己的绑定码，或者输入对方的绑定码进行绑定
 */
import { ref, onMounted, computed } from 'vue'
import { get, post } from '../../utils/request'
import { getUserInfo } from '../../utils/auth'

// 我的绑定码
const bindCode = ref('')
// 输入的绑定码
const inputBindCode = ref('')
// 复制状态
const copyText = ref('复制')
// 伴侣信息
const partnerInfo = ref(null)

// 已绑定
const hasPartner = computed(() => {
  const userInfo = getUserInfo()
  return userInfo && userInfo.partnerId
})

// 是否可以绑定
const canBind = computed(() => {
  return inputBindCode.value.length === 6 && !hasPartner.value
})

// 获取绑定码
async function fetchBindCode() {
  try {
    const code = await get('/user/bind-code')
    bindCode.value = code
    
    // 同时更新本地存储
    const userInfo = getUserInfo()
    if (userInfo) {
      userInfo.bindCode = code
      uni.setStorageSync('userInfo', JSON.stringify(userInfo))
    }
  } catch (error) {
    console.error('获取绑定码失败', error)
  }
}

// 获取伴侣信息
async function fetchPartnerInfo() {
  try {
    const partner = await get('/user/partner')
    if (partner) {
      partnerInfo.value = partner
    }
  } catch (error) {
    console.error('获取伴侣信息失败', error)
  }
}

// 复制绑定码
function copyBindCode() {
  if (!bindCode.value) return
  
  uni.setClipboardData({
    data: bindCode.value,
    success: () => {
      copyText.value = '已复制'
      setTimeout(() => {
        copyText.value = '复制'
      }, 2000)
    }
  })
}

// 输入变化
function onInputChange() {
  // 自动转大写
  inputBindCode.value = inputBindCode.value.toUpperCase()
}

// 执行绑定
async function handleBind() {
  if (!canBind.value) return
  
  try {
    uni.showLoading({ title: '绑定中...' })
    
    const res = await post('/user/bind', { bindCode: inputBindCode.value })
    
    uni.hideLoading()
    
    uni.showToast({
      title: '绑定成功！💕',
      icon: 'success',
      duration: 2000
    })
    
    // 更新本地存储
    const userInfo = getUserInfo()
    if (userInfo && res.currentUser) {
      userInfo.partnerId = res.currentUser.partnerId
      uni.setStorageSync('userInfo', JSON.stringify(userInfo))
    }
    
    // 刷新伴侣信息
    await fetchPartnerInfo()
    
    // 清空输入
    inputBindCode.value = ''
    
  } catch (error) {
    uni.hideLoading()
    uni.showToast({
      title: error.message || '绑定失败，请重试',
      icon: 'none',
      duration: 2000
    })
  }
}

onMounted(() => {
  fetchBindCode()
  if (hasPartner.value) {
    fetchPartnerInfo()
  }
})
</script>

<style lang="scss" scoped>
.page-bind {
  min-height: 100vh;
  background: linear-gradient(180deg, #FFF5F7 0%, #ffffff 100%);
  padding: 40rpx 32rpx;
  box-sizing: border-box;
}

/* 头部装饰 */
.bind-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;
  padding-top: 40rpx;
}

.header-emoji {
  font-size: 120rpx;
  margin-bottom: 24rpx;
  animation: heartbeat 1.5s infinite;
}

@keyframes heartbeat {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.header-title {
  font-size: 44rpx;
  font-weight: 700;
  color: #333333;
  margin-bottom: 12rpx;
}

.header-desc {
  font-size: 26rpx;
  color: #999999;
}

/* 通用section */
.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 20rpx;
  display: block;
}

/* 绑定码部分 */
.bind-code-section {
  margin-bottom: 50rpx;
}

.bind-code-card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 40rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.08);
}

.bind-code,
.bind-code-empty {
  font-size: 48rpx;
  font-weight: 700;
  letter-spacing: 8rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.bind-code-empty {
  color: #cccccc;
  -webkit-text-fill-color: #cccccc;
  background: none;
}

.copy-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 16rpx 24rpx;
  background: #FFF5F7;
  border-radius: 20rpx;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.95);
  }
}

.copy-icon {
  font-size: 28rpx;
}

.copy-text {
  font-size: 26rpx;
  color: #FF6B9D;
  font-weight: 500;
}

.bind-tip {
  font-size: 24rpx;
  color: #999999;
  margin-top: 20rpx;
  display: block;
  text-align: center;
}

/* 输入绑定码部分 */
.bind-input-section {
  margin-bottom: 50rpx;
}

.bind-input-card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 32rpx;
  display: flex;
  gap: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.08);
}

.bind-input {
  flex: 1;
  height: 96rpx;
  background: #f9f9f9;
  border: 2rpx solid #eeeeee;
  border-radius: 16rpx;
  padding: 0 24rpx;
  font-size: 36rpx;
  letter-spacing: 4rpx;
  text-transform: uppercase;
  text-align: center;
  font-weight: 600;
  box-sizing: border-box;
  transition: all 0.3s ease;

  &:focus {
    border-color: #FF6B9D;
    background: #ffffff;
    box-shadow: 0 0 0 6rpx rgba(255, 107, 157, 0.08);
  }
}

.bind-btn {
  padding: 0 40rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 16rpx;
  font-size: 28rpx;
  color: #ffffff;
  font-weight: 600;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;

  &:active {
    transform: scale(0.97);
  }

  &.disabled {
    background: #e0e0e0;
    color: #999999;
  }
}

/* 绑定状态 */
.bind-status-section {
  margin-bottom: 50rpx;
}

.bind-status-card {
  background: linear-gradient(135deg, #FFF5F7, #FFE4EC);
  border-radius: 24rpx;
  padding: 40rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.12);
}

.status-icon {
  font-size: 80rpx;
}

.status-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #FF6B9D;
}

.partner-info {
  margin-top: 16rpx;
}

.partner-name {
  font-size: 36rpx;
  font-weight: 700;
  color: #333333;
}

/* 帮助说明 */
.help-section {
  margin-top: 40rpx;
}

.help-card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(255, 107, 157, 0.08);
}

.help-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 20rpx;
  display: block;
}

.help-item {
  display: flex;
  align-items: flex-start;
  gap: 12rpx;
  margin-bottom: 12rpx;
}

.help-dot {
  font-size: 24rpx;
  color: #FF6B9D;
  margin-top: 4rpx;
}

.help-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
}
</style>
