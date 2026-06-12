<template>
  <!-- 倒计时组件 -->
  <view class="countdown-container">
    <view class="countdown-content">
      <!-- 天数显示 -->
      <view class="days-display">
        <text class="days-number">{{ days }}</text>
        <text class="days-label">{{ isToday ? '今天' : '天' }}</text>
      </view>
      <!-- 爱心动画 -->
      <view class="heart-decoration">
        <text class="heart-icon">💕</text>
      </view>
      <!-- 描述文字 -->
      <text class="countdown-desc">{{ displayText }}</text>
    </view>
    <!-- 飘落爱心 -->
    <view class="floating-hearts">
      <text
        v-for="i in 5"
        :key="i"
        class="floating-heart"
        :style="{ animationDelay: `${i * 0.6}s`, left: `${10 + i * 18}%` }"
      >❤️</text>
    </view>
  </view>
</template>

<script setup>
/**
 * 倒计时组件
 * 用于纪念日倒计时、在一起天数显示
 */
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getDaysTogether, getAnniversaryCountdown } from '../../utils/date.js'

const props = defineProps({
  /** 目标日期 */
  targetDate: {
    type: String,
    required: true
  },
  /** 倒计时类型：together=在一起天数, countdown=倒计时 */
  type: {
    type: String,
    default: 'together'
  },
  /** 描述文字 */
  description: {
    type: String,
    default: ''
  }
})

// 天数
const days = ref(0)
// 是否今天
const isToday = ref(false)
// 更新定时器
let timer = null

// 显示文字
const displayText = computed(() => {
  if (props.description) return props.description
  if (props.type === 'together') return '我们在一起'
  return '距离纪念日'
})

/**
 * 计算天数
 */
function calculate() {
  if (props.type === 'together') {
    days.value = getDaysTogether(props.targetDate)
  } else {
    const result = getAnniversaryCountdown(props.targetDate)
    days.value = result.days
    isToday.value = result.isToday
  }
}

onMounted(() => {
  calculate()
  // 每分钟更新一次
  timer = setInterval(calculate, 60000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style lang="scss" scoped>
.countdown-container {
  position: relative;
  overflow: hidden;
}

.countdown-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 2;
}

.days-display {
  display: flex;
  align-items: baseline;
  gap: 8rpx;

  .days-number {
    font-size: 80rpx;
    font-weight: 800;
    background: linear-gradient(135deg, #FF6B9D, #FF4757);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    line-height: 1;
  }

  .days-label {
    font-size: 28rpx;
    color: #FF6B9D;
    font-weight: 500;
  }
}

.heart-decoration {
  margin: 8rpx 0;

  .heart-icon {
    font-size: 36rpx;
    animation: heartbeat 1.5s infinite;
  }
}

.countdown-desc {
  font-size: 24rpx;
  color: #999999;
}

// 飘落爱心
.floating-hearts {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  z-index: 1;
}

.floating-heart {
  position: absolute;
  top: -20rpx;
  font-size: 24rpx;
  opacity: 0;
  animation: heartFall 3s infinite;
}
</style>
