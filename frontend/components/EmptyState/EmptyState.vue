<template>
  <!-- 空状态组件 -->
  <view class="empty-state">
    <view class="empty-icon-wrapper">
      <text class="empty-icon">{{ icon }}</text>
    </view>
    <text class="empty-title">{{ title }}</text>
    <text v-if="description" class="empty-desc">{{ description }}</text>
    <view v-if="actionText" class="empty-action" @click="handleAction">
      <text class="action-text">{{ actionText }}</text>
    </view>
  </view>
</template>

<script setup>
/**
 * 空状态组件
 * 用于列表为空、无数据等场景的友好提示
 */
const props = defineProps({
  /** 空状态图标emoji */
  icon: {
    type: String,
    default: '📭'
  },
  /** 标题文字 */
  title: {
    type: String,
    default: '暂无数据'
  },
  /** 描述文字 */
  description: {
    type: String,
    default: ''
  },
  /** 操作按钮文字 */
  actionText: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['action'])

/**
 * 点击操作按钮
 */
function handleAction() {
  emit('action')
}
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 60rpx;
  animation: fadeIn 0.5s ease;
}

.empty-icon-wrapper {
  width: 200rpx;
  height: 200rpx;
  background: linear-gradient(135deg, #FFF0F3, #FFE4EC);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40rpx;

  .empty-icon {
    font-size: 80rpx;
    animation: pulse 2s infinite;
  }
}

.empty-title {
  font-size: 32rpx;
  color: #666666;
  font-weight: 500;
  margin-bottom: 12rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: #999999;
  text-align: center;
  line-height: 1.6;
  margin-bottom: 40rpx;
}

.empty-action {
  padding: 16rpx 48rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 40rpx;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 157, 0.3);

  &:active {
    transform: scale(0.95);
  }

  .action-text {
    font-size: 28rpx;
    color: #ffffff;
    font-weight: 500;
  }
}
</style>
