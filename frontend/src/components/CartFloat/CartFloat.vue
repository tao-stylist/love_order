<template>
  <!-- 购物车浮动按钮 -->
  <view class="cart-float" :class="{ 'has-items': totalCount > 0 }" @click="handleClick">
    <!-- 购物车图标 -->
    <view class="cart-icon-wrapper">
      <text class="cart-icon">🛒</text>
      <!-- 数量角标 -->
      <view v-if="totalCount > 0" class="cart-badge">
        <text class="badge-text">{{ totalCount > 99 ? '99+' : totalCount }}</text>
      </view>
    </view>
    <!-- 总价 -->
    <view v-if="totalCount > 0" class="cart-info">
      <text class="cart-price">¥{{ totalPrice.toFixed(1) }}</text>
      <text class="cart-hint">去结算</text>
    </view>
    <!-- 空购物车 -->
    <text v-else class="cart-empty-text">购物车是空的</text>
  </view>
</template>

<script setup>
/**
 * 购物车浮动按钮组件
 * 显示购物车商品数量和总价，点击跳转到下单页
 */
import { computed } from 'vue'
import { useOrderStore } from '../../store/order.js'

const orderStore = useOrderStore()

// 购物车商品总数
const totalCount = computed(() => orderStore.cartTotalCount)

// 购物车总价
const totalPrice = computed(() => orderStore.cartTotalPrice)

/**
 * 点击购物车
 */
function handleClick() {
  if (totalCount.value === 0) {
    uni.showToast({
      title: '购物车是空的，去逛逛吧~',
      icon: 'none'
    })
    return
  }

  uni.navigateTo({
    url: '/pages/order/order'
  })
}
</script>

<style lang="scss" scoped>
.cart-float {
  position: fixed;
  bottom: 140rpx;
  left: 24rpx;
  right: 24rpx;
  height: 100rpx;
  background: #333333;
  border-radius: 50rpx;
  display: flex;
  align-items: center;
  padding: 0 24rpx;
  z-index: 998;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;

  &.has-items {
    background: linear-gradient(135deg, #FF6B9D, #FFA07A);
    box-shadow: 0 8rpx 30rpx rgba(255, 107, 157, 0.35);
  }

  &:active {
    transform: scale(0.98);
  }
}

.cart-icon-wrapper {
  position: relative;
  width: 72rpx;
  height: 72rpx;
  background: #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);

  .cart-icon {
    font-size: 36rpx;
  }
}

.cart-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  min-width: 32rpx;
  height: 32rpx;
  background: #FF4757;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
  border: 2rpx solid #ffffff;

  .badge-text {
    font-size: 20rpx;
    color: #ffffff;
    font-weight: 600;
  }
}

.cart-info {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-left: 20rpx;
  padding-right: 12rpx;

  .cart-price {
    font-size: 36rpx;
    color: #ffffff;
    font-weight: 700;
  }

  .cart-hint {
    font-size: 26rpx;
    color: #ffffff;
    background: rgba(255, 255, 255, 0.25);
    padding: 8rpx 28rpx;
    border-radius: 30rpx;
    font-weight: 500;
  }
}

.cart-empty-text {
  flex: 1;
  text-align: center;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.6);
  margin-left: 20rpx;
}
</style>
