<template>
  <!-- 下单确认页 -->
  <view class="page-order">
    <!-- 收单人信息 -->
    <view class="section-card">
      <view class="section-title-row">
        <text class="section-icon">📍</text>
        <text class="section-title">收单人信息</text>
      </view>
      <view class="address-form">
        <view class="form-item">
          <text class="form-label">姓名</text>
          <input
            class="form-input"
            type="text"
            placeholder="请输入收单人姓名"
            v-model="form.name"
            placeholder-class="input-placeholder"
          />
        </view>
        <view class="form-item">
          <text class="form-label">电话</text>
          <input
            class="form-input"
            type="number"
            placeholder="请输入联系电话"
            v-model="form.phone"
            placeholder-class="input-placeholder"
          />
        </view>
        <view class="form-item">
          <text class="form-label">地址</text>
          <input
            class="form-input"
            type="text"
            placeholder="请输入配送地址"
            v-model="form.address"
            placeholder-class="input-placeholder"
          />
        </view>
      </view>
    </view>

    <!-- 菜品清单 -->
    <view class="section-card">
      <view class="section-title-row">
        <text class="section-icon">🍱</text>
        <text class="section-title">菜品清单</text>
        <text class="item-count">共{{ cartTotalCount }}件</text>
      </view>
      <view class="cart-items">
        <view v-for="item in cartList" :key="item.menuId" class="cart-item">
          <view class="cart-item-left">
            <text class="cart-item-name">{{ item.name }}</text>
            <text class="cart-item-desc">{{ item.desc }}</text>
          </view>
          <view class="cart-item-right">
            <text class="cart-item-quantity">x{{ item.quantity }}</text>
            <text class="cart-item-price">¥{{ (item.price * item.quantity).toFixed(1) }}</text>
          </view>
        </view>
      </view>
      <!-- 合计 -->
      <view class="total-row">
        <text class="total-label">合计</text>
        <text class="total-price">¥{{ cartTotalPrice.toFixed(1) }}</text>
      </view>
    </view>

    <!-- 期望送达时间 -->
    <view class="section-card">
      <view class="section-title-row">
        <text class="section-icon">⏰</text>
        <text class="section-title">期望送达时间</text>
      </view>
      <view class="time-options">
        <view
          v-for="(time, idx) in deliveryTimes"
          :key="idx"
          class="time-option"
          :class="{ active: form.deliveryTime === time.value }"
          @click="form.deliveryTime = time.value"
        >
          <text class="time-label">{{ time.label }}</text>
        </view>
      </view>
    </view>

    <!-- 甜蜜备注 -->
    <view class="section-card">
      <view class="section-title-row">
        <text class="section-icon">💌</text>
        <text class="section-title">甜蜜备注</text>
      </view>
      <textarea
        class="remark-input"
        placeholder="写点什么给TA吧~（如：多加一份爱❤️）"
        placeholder-class="input-placeholder"
        v-model="form.remark"
        maxlength="200"
        :auto-height="true"
      />
      <view class="quick-remarks">
        <text
          v-for="remark in quickRemarks"
          :key="remark"
          class="quick-remark-tag"
          @click="form.remark = remark"
        >{{ remark }}</text>
      </view>
    </view>

    <!-- 爱心积分抵扣 -->
    <view class="section-card">
      <view class="section-title-row">
        <text class="section-icon">💝</text>
        <text class="section-title">爱心积分抵扣</text>
      </view>
      <view class="points-row">
        <view class="points-info">
          <text class="points-label">可用积分</text>
          <text class="points-value">{{ userStore.lovePoints }} 💕</text>
        </view>
        <view class="points-switch" @click="togglePoints">
          <view class="switch-track" :class="{ active: usePoints }">
            <view class="switch-thumb"></view>
          </view>
          <text class="switch-text">{{ usePoints ? `抵扣¥${deductAmount}` : '不使用' }}</text>
        </view>
      </view>
    </view>

    <!-- 底部提交栏 -->
    <view class="submit-bar safe-bottom">
      <view class="submit-info">
        <text class="submit-label">实付：</text>
        <text class="submit-price">¥{{ finalPrice.toFixed(1) }}</text>
        <text v-if="usePoints" class="submit-discount">（已抵扣¥{{ deductAmount }}）</text>
      </view>
      <view class="submit-btn" @click="handleSubmit">
        <text class="submit-btn-text">💕 提交订单</text>
      </view>
    </view>

    <!-- 底部占位 -->
    <view style="height: 160rpx;"></view>
  </view>
</template>

<script setup>
/**
 * 下单确认页
 * 收单人信息、菜品清单、送达时间、备注、积分抵扣
 */
import { ref, computed, onMounted } from 'vue'
import { useOrderStore } from '../../store/order.js'
import { useUserStore } from '../../store/user.js'
import { getDeliveryTimeOptions } from '../../utils/date.js'

const orderStore = useOrderStore()
const userStore = useUserStore()

// 表单数据
const form = ref({
  name: '',
  phone: '',
  address: '',
  deliveryTime: '',
  remark: ''
})

// 是否使用积分
const usePoints = ref(false)

// 快捷备注
const quickRemarks = ref([
  '多加一份爱❤️',
  '少放辣哦~',
  '送到门口就好',
  '记得说加油💪',
  '想你了🥺',
  '今天辛苦啦~'
])

// 送达时间选项
const deliveryTimes = ref([])

// 购物车数据
const cartList = computed(() => orderStore.cartList)
const cartTotalCount = computed(() => orderStore.cartTotalCount)
const cartTotalPrice = computed(() => orderStore.cartTotalPrice)

// 抵扣金额（100积分=1元）
const deductAmount = computed(() => {
  return Math.min(Math.floor(userStore.lovePoints / 100), cartTotalPrice.value)
})

// 最终价格
const finalPrice = computed(() => {
  return Math.max(0, cartTotalPrice.value - (usePoints.value ? deductAmount.value : 0))
})

onMounted(() => {
  // 生成送达时间选项
  deliveryTimes.value = getDeliveryTimeOptions()
  // 默认选择第一个时间
  if (deliveryTimes.value.length > 0) {
    form.value.deliveryTime = deliveryTimes.value[0].value
  }
})

/**
 * 切换积分使用
 */
function togglePoints() {
  usePoints.value = !usePoints.value
}

/**
 * 提交订单
 */
function handleSubmit() {
  // 表单验证
  if (!form.value.name) {
    uni.showToast({ title: '请输入收单人姓名', icon: 'none' })
    return
  }
  if (!form.value.phone) {
    uni.showToast({ title: '请输入联系电话', icon: 'none' })
    return
  }
  if (!form.value.address) {
    uni.showToast({ title: '请输入配送地址', icon: 'none' })
    return
  }
  if (cartList.value.length === 0) {
    uni.showToast({ title: '购物车是空的', icon: 'none' })
    return
  }

  // 提交订单
  const order = orderStore.submitOrder({
    ...form.value,
    usePoints: usePoints.value,
    deductAmount: usePoints.value ? deductAmount.value : 0,
    finalPrice: finalPrice.value
  })

  // 扣除积分
  if (usePoints.value) {
    userStore.updateLovePoints(userStore.lovePoints - deductAmount.value * 100)
  }

  uni.showToast({
    title: '下单成功！💕',
    icon: 'success',
    duration: 2000
  })

  // 跳转到订单详情
  setTimeout(() => {
    uni.redirectTo({
      url: `/pages/orders/detail?id=${order.id}`
    })
  }, 1500)
}
</script>

<style lang="scss" scoped>
.page-order {
  min-height: 100vh;
  background: #FFF5F7;
  padding: 24rpx;
}

/* 通用卡片 */
.section-card {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
  animation: fadeIn 0.4s ease;
}

.section-title-row {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;

  .section-icon {
    font-size: 32rpx;
    margin-right: 8rpx;
  }

  .section-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333333;
    flex: 1;
  }

  .item-count {
    font-size: 24rpx;
    color: #999999;
  }
}

/* 表单 */
.form-item {
  display: flex;
  align-items: center;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #FFF0F3;

  &:last-child {
    border-bottom: none;
  }

  .form-label {
    width: 100rpx;
    font-size: 28rpx;
    color: #666666;
    flex-shrink: 0;
  }

  .form-input {
    flex: 1;
    font-size: 28rpx;
    color: #333333;
    height: 48rpx;
  }
}

.input-placeholder {
  color: #cccccc;
  font-size: 26rpx;
}

/* 购物车清单 */
.cart-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #FFF0F3;

  &:last-child {
    border-bottom: none;
  }
}

.cart-item-left {
  flex: 1;
  display: flex;
  flex-direction: column;

  .cart-item-name {
    font-size: 28rpx;
    color: #333333;
    font-weight: 500;
  }

  .cart-item-desc {
    font-size: 22rpx;
    color: #999999;
    margin-top: 4rpx;
  }
}

.cart-item-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
  flex-shrink: 0;

  .cart-item-quantity {
    font-size: 24rpx;
    color: #999999;
  }

  .cart-item-price {
    font-size: 28rpx;
    color: #FF4757;
    font-weight: 600;
  }
}

.total-row {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-top: 16rpx;
  border-top: 1rpx dashed #FFE4EC;

  .total-label {
    font-size: 28rpx;
    color: #666666;
    margin-right: 8rpx;
  }

  .total-price {
    font-size: 36rpx;
    color: #FF4757;
    font-weight: 700;
  }
}

/* 送达时间 */
.time-options {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.time-option {
  padding: 14rpx 24rpx;
  background: #FFF5F7;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s ease;

  &.active {
    background: #FFF0F3;
    border-color: #FF6B9D;

    .time-label {
      color: #FF6B9D;
      font-weight: 600;
    }
  }

  &:active {
    transform: scale(0.97);
  }

  .time-label {
    font-size: 24rpx;
    color: #666666;
  }
}

/* 备注 */
.remark-input {
  width: 100%;
  min-height: 120rpx;
  font-size: 26rpx;
  color: #333333;
  line-height: 1.6;
  padding: 16rpx;
  background: #FFF5F7;
  border-radius: 12rpx;
  box-sizing: border-box;
}

.quick-remarks {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-top: 16rpx;
}

.quick-remark-tag {
  font-size: 22rpx;
  color: #FF6B9D;
  background: #FFF0F3;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;

  &:active {
    background: #FFE4EC;
  }
}

/* 积分抵扣 */
.points-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.points-info {
  display: flex;
  align-items: center;
  gap: 12rpx;

  .points-label {
    font-size: 26rpx;
    color: #666666;
  }

  .points-value {
    font-size: 28rpx;
    color: #FF6B9D;
    font-weight: 600;
  }
}

.points-switch {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.switch-track {
  width: 80rpx;
  height: 44rpx;
  background: #e0e0e0;
  border-radius: 22rpx;
  position: relative;
  transition: background 0.3s ease;

  &.active {
    background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  }

  .switch-thumb {
    width: 36rpx;
    height: 36rpx;
    background: #ffffff;
    border-radius: 50%;
    position: absolute;
    top: 4rpx;
    left: 4rpx;
    transition: transform 0.3s ease;
    box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
  }

  &.active .switch-thumb {
    transform: translateX(36rpx);
  }
}

.switch-text {
  font-size: 24rpx;
  color: #999999;
}

/* 底部提交栏 */
.submit-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120rpx;
  background: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32rpx;
  box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.06);
  z-index: 100;
}

.submit-info {
  display: flex;
  align-items: baseline;

  .submit-label {
    font-size: 28rpx;
    color: #333333;
  }

  .submit-price {
    font-size: 44rpx;
    color: #FF4757;
    font-weight: 800;
  }

  .submit-discount {
    font-size: 20rpx;
    color: #FF6B9D;
    margin-left: 8rpx;
  }
}

.submit-btn {
  padding: 20rpx 48rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 50rpx;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 157, 0.4);

  &:active {
    transform: scale(0.95);
  }

  .submit-btn-text {
    font-size: 30rpx;
    color: #ffffff;
    font-weight: 600;
  }
}
</style>
