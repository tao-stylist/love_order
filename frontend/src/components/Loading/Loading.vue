<template>
  <!-- 加载组件 -->
  <view class="loading-container" :class="{ fullscreen: fullscreen }">
    <view class="loading-content">
      <!-- 旋转爱心 -->
      <view class="loading-hearts">
        <text class="heart heart-1">💕</text>
        <text class="heart heart-2">💗</text>
        <text class="heart heart-3">💖</text>
      </view>
      <!-- 加载文字 -->
      <text v-if="text" class="loading-text">{{ text }}</text>
      <!-- 加载点动画 -->
      <view class="loading-dots">
        <view class="dot"></view>
        <view class="dot"></view>
        <view class="dot"></view>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 加载组件
 * 可爱风格的加载动画
 */
defineProps({
  /** 加载提示文字 */
  text: {
    type: String,
    default: '加载中'
  },
  /** 是否全屏显示 */
  fullscreen: {
    type: Boolean,
    default: false
  }
})
</script>

<style lang="scss" scoped>
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60rpx 0;

  &.fullscreen {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 245, 247, 0.9);
    z-index: 9999;
    padding: 0;
  }
}

.loading-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.loading-hearts {
  position: relative;
  width: 120rpx;
  height: 120rpx;
  margin-bottom: 24rpx;

  .heart {
    position: absolute;
    font-size: 40rpx;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .heart-1 {
    animation: rotate 2s linear infinite;
  }

  .heart-2 {
    font-size: 28rpx;
    animation: rotate 2s linear infinite reverse;
    opacity: 0.6;
  }

  .heart-3 {
    font-size: 20rpx;
    animation: rotate 3s linear infinite;
    opacity: 0.3;
  }
}

.loading-text {
  font-size: 26rpx;
  color: #FF6B9D;
  margin-bottom: 16rpx;
}

.loading-dots {
  display: flex;
  gap: 12rpx;

  .dot {
    width: 12rpx;
    height: 12rpx;
    background: #FF6B9D;
    border-radius: 50%;
    animation: blink 1.2s infinite;

    &:nth-child(2) {
      animation-delay: 0.2s;
    }

    &:nth-child(3) {
      animation-delay: 0.4s;
    }
  }
}
</style>
