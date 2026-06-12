<template>
  <!-- 自定义底部导航栏 -->
  <view class="tabbar-container safe-bottom">
    <view
      v-for="(tab, index) in tabs"
      :key="index"
      class="tabbar-item"
      :class="{ active: currentTab === tab.path }"
      @click="switchTab(tab)"
    >
      <view class="tabbar-icon">
        <text class="icon-text">{{ tab.icon }}</text>
        <!-- 选中时的红点 -->
        <view v-if="tab.dot" class="tabbar-dot"></view>
      </view>
      <text class="tabbar-label">{{ tab.label }}</text>
      <!-- 选中指示器 -->
      <view v-if="currentTab === tab.path" class="tabbar-indicator"></view>
    </view>
  </view>
</template>

<script setup>
/**
 * 自定义底部导航栏组件
 * 首页、菜单、订单、我的
 */
import { ref, computed } from 'vue'

const props = defineProps({
  /** 当前选中的tab路径 */
  current: {
    type: String,
    default: '/pages/index/index'
  }
})

// tab列表
const tabs = ref([
  {
    label: '首页',
    icon: '🏠',
    activeIcon: '🏡',
    path: '/pages/index/index',
    dot: false
  },
  {
    label: '菜单',
    icon: '📋',
    activeIcon: '📝',
    path: '/pages/menu/menu',
    dot: false
  },
  {
    label: '订单',
    icon: '📦',
    activeIcon: '📋',
    path: '/pages/orders/orders',
    dot: true
  },
  {
    label: '我的',
    icon: '👤',
    activeIcon: '😊',
    path: '/pages/profile/profile',
    dot: false
  }
])

// 当前选中的tab
const currentTab = computed(() => props.current)

/**
 * 切换tab
 */
function switchTab(tab) {
  if (currentTab.value === tab.path) return

  uni.switchTab({
    url: tab.path
  })
}
</script>

<style lang="scss" scoped>
.tabbar-container {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 110rpx;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -2rpx 20rpx rgba(255, 107, 157, 0.08);
  z-index: 999;
  padding-bottom: env(safe-area-inset-bottom);
}

.tabbar-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  position: relative;
  padding: 10rpx 0;

  &.active {
    .tabbar-icon .icon-text {
      transform: scale(1.15);
    }

    .tabbar-label {
      color: #FF6B9D;
      font-weight: 600;
    }
  }
}

.tabbar-icon {
  position: relative;
  width: 52rpx;
  height: 52rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  .icon-text {
    font-size: 44rpx;
    transition: transform 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  }
}

.tabbar-dot {
  position: absolute;
  top: 0;
  right: 0;
  width: 14rpx;
  height: 14rpx;
  background: #FF4757;
  border-radius: 50%;
  border: 2rpx solid #ffffff;
  animation: blink 1.5s infinite;
}

.tabbar-label {
  font-size: 20rpx;
  color: #999999;
  margin-top: 4rpx;
  transition: all 0.3s ease;
}

.tabbar-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 4rpx;
  background: linear-gradient(90deg, #FF6B9D, #FFA07A);
  border-radius: 2rpx;
  animation: fadeIn 0.3s ease;
}
</style>
