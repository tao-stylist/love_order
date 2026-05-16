<template>
  <!-- 订单详情页 -->
  <view class="page-detail">
    <!-- 订单状态头部 -->
    <view class="status-header" :style="{ background: statusBg }">
      <text class="status-icon">{{ statusIcon }}</text>
      <text class="status-text">{{ order?.statusText || '加载中...' }}</text>
      <text class="status-desc">{{ statusDesc }}</text>
    </view>

    <!-- 订单状态流程 -->
    <view class="progress-section">
      <view class="progress-steps">
        <view
          v-for="(step, idx) in steps"
          :key="idx"
          class="step-item"
          :class="{ active: idx <= currentStep, current: idx === currentStep }"
        >
          <view class="step-dot-wrapper">
            <view class="step-dot">
              <text class="step-emoji">{{ step.icon }}</text>
            </view>
            <view v-if="idx < steps.length - 1" class="step-line" :class="{ active: idx < currentStep }"></view>
          </view>
          <text class="step-label">{{ step.label }}</text>
        </view>
      </view>
    </view>

    <!-- 收单人信息 -->
    <view class="section-card">
      <view class="card-title">
        <text class="card-title-icon">📍</text>
        <text class="card-title-text">收单人信息</text>
      </view>
      <view class="info-row">
        <text class="info-label">姓名</text>
        <text class="info-value">{{ order?.name || '未填写' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">电话</text>
        <text class="info-value">{{ order?.phone || '未填写' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">地址</text>
        <text class="info-value">{{ order?.address || '未填写' }}</text>
      </view>
    </view>

    <!-- 菜品明细 -->
    <view class="section-card">
      <view class="card-title">
        <text class="card-title-icon">🍱</text>
        <text class="card-title-text">菜品明细</text>
      </view>
      <view class="detail-items">
        <view v-for="item in order?.items" :key="item.menuId" class="detail-item">
          <view class="detail-item-left">
            <view class="detail-item-emoji">🍽️</view>
            <view class="detail-item-info">
              <text class="detail-item-name">{{ item.name }}</text>
              <text class="detail-item-quantity">x{{ item.quantity }}</text>
            </view>
          </view>
          <text class="detail-item-price">¥{{ (item.price * item.quantity).toFixed(1) }}</text>
        </view>
      </view>

      <!-- 价格汇总 -->
      <view class="price-summary">
        <view class="summary-row">
          <text class="summary-label">商品小计</text>
          <text class="summary-value">¥{{ subtotal.toFixed(1) }}</text>
        </view>
        <view v-if="order?.deductAmount > 0" class="summary-row">
          <text class="summary-label">积分抵扣</text>
          <text class="summary-value discount">-¥{{ order.deductAmount.toFixed(1) }}</text>
        </view>
        <view class="summary-row total">
          <text class="summary-label">实付金额</text>
          <text class="summary-value total-price">¥{{ (order?.finalPrice || subtotal).toFixed(1) }}</text>
        </view>
      </view>
    </view>

    <!-- 订单信息 -->
    <view class="section-card">
      <view class="card-title">
        <text class="card-title-icon">📋</text>
        <text class="card-title-text">订单信息</text>
      </view>
      <view class="info-row">
        <text class="info-label">订单号</text>
        <text class="info-value">{{ order?.orderNo }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">下单时间</text>
        <text class="info-value">{{ formatTime(order?.createTime) }}</text>
      </view>
      <view v-if="order?.deliveryTime" class="info-row">
        <text class="info-label">送达时间</text>
        <text class="info-value">{{ order.deliveryTime }}</text>
      </view>
      <view v-if="order?.remark" class="info-row">
        <text class="info-label">甜蜜备注</text>
        <text class="info-value remark">{{ order.remark }}</text>
      </view>
    </view>

    <!-- 评价入口 -->
    <view v-if="order?.status === 'completed'" class="review-entry" @click="goReview">
      <text class="review-entry-text">💕 为这份爱心订单评价</text>
      <text class="review-entry-arrow">></text>
    </view>

    <!-- 底部操作 -->
    <view v-if="order?.status === 'completed'" class="bottom-actions safe-bottom">
      <view class="bottom-btn reorder" @click="reorder">
        <text>🔄 再来一单</text>
      </view>
    </view>

    <view style="height: 160rpx;"></view>
  </view>
</template>

<script setup>
/**
 * 订单详情页
 * 订单状态流程、菜品明细、评价入口
 */
import { ref, computed, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useOrderStore } from '../../store/order.js'
import { formatDate } from '../../utils/date.js'

const orderStore = useOrderStore()

// 订单数据
const order = ref(null)
const orderId = ref('')

// 状态流程步骤
const steps = ref([
  { label: '待接单', icon: '📋' },
  { label: '制作中', icon: '👨‍🍳' },
  { label: '配送中', icon: '🚴' },
  { label: '已完成', icon: '✨' }
])

// 当前步骤
const currentStep = computed(() => {
  const statusMap = { 'pending': 0, 'accepted': 0, 'making': 1, 'delivering': 2, 'completed': 3 }
  return statusMap[order.value?.status] || 0
})

// 状态图标
const statusIcon = computed(() => {
  const iconMap = { 'pending': '⏳', 'making': '👨‍🍳', 'delivering': '🚴', 'completed': '✨' }
  return iconMap[order.value?.status] || '📋'
})

// 状态描述
const statusDesc = computed(() => {
  const descMap = {
    'pending': '商家正在接单，请耐心等待~',
    'making': '厨师正在用心制作你的美食',
    'delivering': '骑手正在飞奔向你',
    'completed': '订单已完成，希望你吃得开心~'
  }
  return descMap[order.value?.status] || ''
})

// 状态背景色
const statusBg = computed(() => {
  const bgMap = {
    'pending': 'linear-gradient(135deg, #FFA07A, #FFD700)',
    'making': 'linear-gradient(135deg, #4ECDC4, #44B09E)',
    'delivering': 'linear-gradient(135deg, #45B7D1, #96CEB4)',
    'completed': 'linear-gradient(135deg, #FF6B9D, #FFA07A)'
  }
  return bgMap[order.value?.status] || 'linear-gradient(135deg, #FF6B9D, #FFA07A)'
})

// 小计
const subtotal = computed(() => {
  if (!order.value?.items) return 0
  return order.value.items.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

onLoad((options) => {
  orderId.value = options.id
  loadOrderDetail()
})

/**
 * 加载订单详情
 */
function loadOrderDetail() {
  order.value = orderStore.getOrderDetail(orderId.value)
  if (!order.value) {
    // 如果store中没有，使用模拟数据
    order.value = {
      id: orderId.value,
      orderNo: 'LO20240516001',
      name: '小甜心',
      phone: '138****8888',
      address: '幸福小区3栋502',
      items: [
        { menuId: '1', name: '爱心便当', price: 28.8, quantity: 1, desc: '满满都是爱意的便当~' },
        { menuId: '9', name: '珍珠奶茶', price: 13.8, quantity: 2, desc: 'Q弹珍珠，丝滑奶茶' }
      ],
      totalPrice: 56.4,
      status: 'completed',
      statusText: '已完成',
      createTime: '2024-05-15T12:30:00',
      deliveryTime: '2024-05-15 13:00',
      remark: '少放辣哦~',
      deductAmount: 0,
      finalPrice: 56.4
    }
  }
}

/**
 * 格式化时间
 */
function formatTime(time) {
  if (!time) return ''
  return formatDate(time, 'YYYY-MM-DD HH:mm')
}

/**
 * 跳转到评价页
 */
function goReview() {
  uni.navigateTo({
    url: `/pages/review/review?orderId=${orderId.value}`
  })
}

/**
 * 再来一单
 */
function reorder() {
  if (order.value?.items) {
    order.value.items.forEach(item => {
      orderStore.addToCart({
        id: item.menuId,
        name: item.name,
        price: item.price,
        desc: item.desc || ''
      })
    })
  }
  uni.switchTab({
    url: '/pages/menu/menu'
  })
}
</script>

<style lang="scss" scoped>
.page-detail {
  min-height: 100vh;
  background: #FFF5F7;
}

/* 状态头部 */
.status-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 32rpx 48rpx;

  .status-icon {
    font-size: 64rpx;
    margin-bottom: 16rpx;
    animation: scaleIn 0.5s ease;
  }

  .status-text {
    font-size: 36rpx;
    font-weight: 700;
    color: #ffffff;
    margin-bottom: 8rpx;
  }

  .status-desc {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

/* 状态流程 */
.progress-section {
  background: #ffffff;
  margin: -24rpx 24rpx 20rpx;
  border-radius: 20rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 2;
}

.progress-steps {
  display: flex;
  justify-content: space-between;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;

  &.active .step-dot {
    background: linear-gradient(135deg, #FF6B9D, #FFA07A);
    box-shadow: 0 4rpx 12rpx rgba(255, 107, 157, 0.3);
  }

  &.current .step-dot {
    animation: pulse 2s infinite;
  }

  &:not(.active) .step-dot {
    background: #f0f0f0;
  }

  &:not(.active) .step-label {
    color: #cccccc;
  }
}

.step-dot-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
}

.step-dot {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.3s ease;

  .step-emoji {
    font-size: 28rpx;
  }
}

.step-line {
  flex: 1;
  height: 4rpx;
  background: #f0f0f0;
  margin: 0 8rpx;
  transition: background 0.3s ease;

  &.active {
    background: linear-gradient(90deg, #FF6B9D, #FFA07A);
  }
}

.step-label {
  font-size: 22rpx;
  color: #666666;
  margin-top: 12rpx;
  transition: color 0.3s ease;
}

/* 通用卡片 */
.section-card {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin: 0 24rpx 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.card-title {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #FFF0F3;

  .card-title-icon {
    font-size: 28rpx;
    margin-right: 8rpx;
  }

  .card-title-text {
    font-size: 30rpx;
    font-weight: 600;
    color: #333333;
  }
}

.info-row {
  display: flex;
  align-items: flex-start;
  padding: 12rpx 0;

  .info-label {
    width: 140rpx;
    font-size: 26rpx;
    color: #999999;
    flex-shrink: 0;
  }

  .info-value {
    flex: 1;
    font-size: 26rpx;
    color: #333333;

    &.remark {
      color: #FF6B9D;
      font-style: italic;
    }
  }
}

/* 菜品明细 */
.detail-items {
  margin-bottom: 20rpx;
}

.detail-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #FFF0F3;

  &:last-child {
    border-bottom: none;
  }
}

.detail-item-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.detail-item-emoji {
  font-size: 36rpx;
}

.detail-item-info {
  display: flex;
  align-items: center;
  gap: 12rpx;

  .detail-item-name {
    font-size: 28rpx;
    color: #333333;
  }

  .detail-item-quantity {
    font-size: 24rpx;
    color: #999999;
  }
}

.detail-item-price {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

/* 价格汇总 */
.price-summary {
  padding-top: 16rpx;
  border-top: 1rpx dashed #FFE4EC;
}

.summary-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8rpx 0;

  &.total {
    padding-top: 16rpx;
    margin-top: 8rpx;
    border-top: 1rpx solid #FFE4EC;
  }

  .summary-label {
    font-size: 26rpx;
    color: #666666;
  }

  .summary-value {
    font-size: 26rpx;
    color: #333333;

    &.discount {
      color: #FF6B9D;
    }

    &.total-price {
      font-size: 36rpx;
      color: #FF4757;
      font-weight: 700;
    }
  }
}

/* 评价入口 */
.review-entry {
  margin: 0 24rpx 20rpx;
  background: linear-gradient(135deg, #FFF0F3, #FFE4EC);
  border-radius: 16rpx;
  padding: 24rpx 28rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;

  &:active {
    transform: scale(0.98);
  }

  .review-entry-text {
    font-size: 28rpx;
    color: #FF6B9D;
    font-weight: 500;
  }

  .review-entry-arrow {
    font-size: 28rpx;
    color: #FF6B9D;
  }
}

/* 底部操作 */
.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16rpx 24rpx;
  background: #ffffff;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.06);
  z-index: 100;
}

.bottom-btn {
  padding: 20rpx;
  text-align: center;
  border-radius: 50rpx;
  font-size: 28rpx;
  font-weight: 500;

  &.reorder {
    background: linear-gradient(135deg, #FF6B9D, #FFA07A);
    color: #ffffff;

    &:active {
      transform: scale(0.97);
    }
  }
}
</style>
