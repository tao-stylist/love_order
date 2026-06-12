<template>
  <!-- 纪念日管理页 -->
  <view class="page-anniversary">
    <!-- 纪念日列表 -->
    <view v-if="anniversaries.length > 0" class="anniversary-list">
      <view
        v-for="item in anniversaries"
        :key="item.id"
        class="anniversary-card"
      >
        <view class="card-left">
          <text class="anniversary-emoji">{{ item.emoji || '💕' }}</text>
        </view>
        <view class="card-center">
          <text class="anniversary-name">{{ item.name }}</text>
          <text class="anniversary-date">{{ item.date }}</text>
        </view>
        <view class="card-right">
          <view class="countdown-badge">
            <text class="countdown-days">{{ getCountdown(item.date) }}</text>
            <text class="countdown-label">天</text>
          </view>
          <view class="delete-btn" @click="handleDelete(item)">
            <text class="delete-icon">🗑️</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <EmptyState
      v-else
      icon="💕"
      title="还没有纪念日"
      description="添加一个重要的日子，让爱更有仪式感~"
    />

    <!-- 添加纪念日表单 -->
    <view class="add-section">
      <view class="add-title">
        <text class="add-icon">✨</text>
        <text class="add-text">添加纪念日</text>
      </view>

      <view class="form-card">
        <!-- 纪念日名称 -->
        <view class="form-item">
          <text class="form-label">名称</text>
          <input
            class="form-input"
            type="text"
            placeholder="如：在一起的日子"
            v-model="form.name"
            placeholder-class="input-placeholder"
          />
        </view>

        <!-- 纪念日日期 -->
        <view class="form-item">
          <text class="form-label">日期</text>
          <picker mode="date" @change="onDateChange">
            <view class="picker-display">
              <text :class="['picker-text', { placeholder: !form.date }]">
                {{ form.date || '请选择日期' }}
              </text>
              <text class="picker-arrow">📅</text>
            </view>
          </picker>
        </view>

        <!-- emoji选择 -->
        <view class="form-item">
          <text class="form-label">图标</text>
          <view class="emoji-picker">
            <view
              v-for="emoji in emojiList"
              :key="emoji"
              class="emoji-item"
              :class="{ active: form.emoji === emoji }"
              @click="form.emoji = emoji"
            >
              <text class="emoji-text">{{ emoji }}</text>
            </view>
          </view>
        </view>

        <!-- 提交按钮 -->
        <view class="submit-btn" @click="handleSubmit">
          <text class="submit-btn-text">💕 添加纪念日</text>
        </view>
      </view>
    </view>

    <!-- 底部占位 -->
    <view style="height: 60rpx;"></view>
  </view>
</template>

<script setup>
/**
 * 纪念日管理页
 * 纪念日列表、添加纪念日、倒计时显示
 */
import { ref, computed } from 'vue'
import { useUserStore } from '../../store/user.js'
import { getAnniversaryCountdown } from '../../utils/date.js'
import EmptyState from '../../components/EmptyState/EmptyState.vue'

const userStore = useUserStore()

// 纪念日列表
const anniversaries = computed(() => userStore.anniversaries)

// 表单数据
const form = ref({
  name: '',
  date: '',
  emoji: '💕'
})

// emoji选择列表
const emojiList = ref(['💕', '💗', '💖', '💝', '💘', '🎂', '🎉', '🌹', '💍', '🏠'])

/**
 * 获取倒计时天数
 */
function getCountdown(date) {
  const result = getAnniversaryCountdown(date)
  return result.days
}

/**
 * 日期选择
 */
function onDateChange(e) {
  form.value.date = e.detail.value
}

/**
 * 提交添加
 */
function handleSubmit() {
  if (!form.value.name) {
    uni.showToast({ title: '请输入纪念日名称', icon: 'none' })
    return
  }
  if (!form.value.date) {
    uni.showToast({ title: '请选择日期', icon: 'none' })
    return
  }

  userStore.addAnniversary({
    name: form.value.name,
    date: form.value.date,
    emoji: form.value.emoji
  })

  // 如果是第一个纪念日且用户没设置在一起日期，自动设置
  if (anniversaries.value.length === 1 && !userStore.loveStartDate) {
    userStore.setLoveStartDate(form.value.date)
  }

  uni.showToast({
    title: '纪念日添加成功！💕',
    icon: 'success'
  })

  // 重置表单
  form.value = { name: '', date: '', emoji: '💕' }
}

/**
 * 删除纪念日
 */
function handleDelete(item) {
  uni.showModal({
    title: '提示',
    content: `确定要删除「${item.name}」吗？`,
    confirmColor: '#FF6B9D',
    success: (res) => {
      if (res.confirm) {
        userStore.removeAnniversary(item.id)
        uni.showToast({
          title: '已删除',
          icon: 'success'
        })
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.page-anniversary {
  min-height: 100vh;
  background: #FFF5F7;
  padding: 24rpx;
}

/* 纪念日列表 */
.anniversary-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  margin-bottom: 32rpx;
}

.anniversary-card {
  display: flex;
  align-items: center;
  background: #ffffff;
  border-radius: 20rpx;
  padding: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
  animation: fadeIn 0.4s ease;
}

.card-left {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #FFF0F3, #FFE4EC);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;

  .anniversary-emoji {
    font-size: 40rpx;
  }
}

.card-center {
  flex: 1;
  margin-left: 20rpx;

  .anniversary-name {
    font-size: 30rpx;
    font-weight: 600;
    color: #333333;
    display: block;
  }

  .anniversary-date {
    font-size: 24rpx;
    color: #999999;
    margin-top: 4rpx;
    display: block;
  }
}

.card-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.countdown-badge {
  display: flex;
  align-items: baseline;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  padding: 8rpx 20rpx;
  border-radius: 12rpx;

  .countdown-days {
    font-size: 36rpx;
    font-weight: 800;
    color: #ffffff;
    line-height: 1;
  }

  .countdown-label {
    font-size: 20rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-left: 4rpx;
  }
}

.delete-btn {
  width: 56rpx;
  height: 56rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  &:active {
    transform: scale(0.9);
  }

  .delete-icon {
    font-size: 28rpx;
  }
}

/* 添加区域 */
.add-section {
  margin-top: 16rpx;
}

.add-title {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 20rpx;

  .add-icon {
    font-size: 28rpx;
  }

  .add-text {
    font-size: 30rpx;
    font-weight: 600;
    color: #333333;
  }
}

.form-card {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.form-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #FFF0F3;

  &:last-of-type {
    border-bottom: none;
  }

  .form-label {
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 12rpx;
    display: block;
  }

  .form-input {
    width: 100%;
    font-size: 28rpx;
    color: #333333;
    height: 48rpx;
  }
}

.input-placeholder {
  color: #cccccc;
  font-size: 26rpx;
}

.picker-display {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12rpx 16rpx;
  background: #FFF5F7;
  border-radius: 12rpx;

  .picker-text {
    font-size: 28rpx;
    color: #333333;

    &.placeholder {
      color: #cccccc;
    }
  }

  .picker-arrow {
    font-size: 28rpx;
  }
}

.emoji-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.emoji-item {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #FFF5F7;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s ease;

  &.active {
    background: #FFE4EC;
    border-color: #FF6B9D;
    transform: scale(1.1);
  }

  &:active {
    transform: scale(0.9);
  }

  .emoji-text {
    font-size: 32rpx;
  }
}

.submit-btn {
  margin-top: 32rpx;
  padding: 24rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 50rpx;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 157, 0.4);

  &:active {
    transform: scale(0.97);
  }

  .submit-btn-text {
    font-size: 30rpx;
    color: #ffffff;
    font-weight: 600;
  }
}
</style>
