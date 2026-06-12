<template>
  <!-- 评价页 -->
  <view class="page-review">
    <!-- 订单信息 -->
    <view class="order-info-card">
      <view class="order-info-header">
        <text class="order-info-icon">📦</text>
        <text class="order-info-title">评价订单</text>
      </view>
      <view class="order-items-preview">
        <view v-for="item in orderItems" :key="item.menuId" class="preview-item">
          <text class="preview-emoji">🍽️</text>
          <text class="preview-name">{{ item.name }}</text>
          <text class="preview-quantity">x{{ item.quantity }}</text>
        </view>
      </view>
    </view>

    <!-- 爱心评分 -->
    <view class="rating-section">
      <text class="section-title">💕 为这份爱心美食打分</text>
      <HeartRating v-model="rating" :max-count="5" :show-text="true" />
    </view>

    <!-- 评价标签 -->
    <view class="tags-section">
      <text class="section-title">🏷️ 选择评价标签</text>
      <view class="tags-list">
        <view
          v-for="tag in tags"
          :key="tag"
          class="tag-item"
          :class="{ active: selectedTags.includes(tag) }"
          @click="toggleTag(tag)"
        >
          <text class="tag-text">{{ tag }}</text>
        </view>
      </view>
    </view>

    <!-- 评价内容 -->
    <view class="content-section">
      <text class="section-title">✍️ 写下你的感受</text>
      <textarea
        class="content-input"
        placeholder="分享你的用餐体验吧~（选填）"
        placeholder-class="input-placeholder"
        v-model="content"
        maxlength="500"
        :auto-height="true"
      />
      <view class="content-footer">
        <text class="char-count">{{ content.length }}/500</text>
        <!-- 添加图片按钮 -->
        <view class="add-image-btn" @click="addImage">
          <text class="add-image-icon">📷</text>
          <text class="add-image-text">添加图片</text>
        </view>
      </view>
    </view>

    <!-- 匿名评价 -->
    <view class="anonymous-section">
      <text class="anonymous-label">匿名评价</text>
      <view class="anonymous-switch" @click="isAnonymous = !isAnonymous">
        <view class="switch-track" :class="{ active: isAnonymous }">
          <view class="switch-thumb"></view>
        </view>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-section safe-bottom">
      <view class="submit-btn" @click="handleSubmit">
        <text class="submit-btn-text">💕 提交评价</text>
      </view>
    </view>

    <!-- 底部占位 -->
    <view style="height: 160rpx;"></view>
  </view>
</template>

<script setup>
/**
 * 评价页
 * 爱心评分、评价标签、评价内容、匿名评价
 */
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useOrderStore } from '../../store/order.js'
import HeartRating from '../../components/HeartRating/HeartRating.vue'

const orderStore = useOrderStore()

// 订单ID
const orderId = ref('')
// 评分
const rating = ref(5)
// 评价标签
const tags = ref(['好吃', '分量足', '配送快', '包装好', '味道赞', '下次还点', '超值', '颜值高'])
const selectedTags = ref([])
// 评价内容
const content = ref('')
// 是否匿名
const isAnonymous = ref(false)
// 订单菜品
const orderItems = ref([])

onLoad((options) => {
  orderId.value = options.orderId
  loadOrderInfo()
})

/**
 * 加载订单信息
 */
function loadOrderInfo() {
  const order = orderStore.getOrderDetail(orderId.value)
  if (order) {
    orderItems.value = order.items
  } else {
    // 模拟数据
    orderItems.value = [
      { menuId: '1', name: '爱心便当', quantity: 1 },
      { menuId: '9', name: '珍珠奶茶', quantity: 2 }
    ]
  }
}

/**
 * 切换标签选择
 */
function toggleTag(tag) {
  const index = selectedTags.value.indexOf(tag)
  if (index > -1) {
    selectedTags.value.splice(index, 1)
  } else {
    if (selectedTags.value.length >= 3) {
      uni.showToast({ title: '最多选择3个标签', icon: 'none' })
      return
    }
    selectedTags.value.push(tag)
  }
}

/**
 * 添加图片
 */
function addImage() {
  uni.chooseImage({
    count: 3,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: () => {
      uni.showToast({ title: '图片上传功能开发中~', icon: 'none' })
    }
  })
}

/**
 * 提交评价
 */
function handleSubmit() {
  if (rating.value === 0) {
    uni.showToast({ title: '请先打个分吧~', icon: 'none' })
    return
  }

  // 模拟提交
  uni.showLoading({ title: '提交中...' })

  setTimeout(() => {
    uni.hideLoading()
    uni.showToast({
      title: '评价成功！感谢你的反馈 💕',
      icon: 'success',
      duration: 2000
    })

    // 返回订单详情
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  }, 1000)
}
</script>

<style lang="scss" scoped>
.page-review {
  min-height: 100vh;
  background: #FFF5F7;
  padding: 24rpx;
}

/* 订单信息卡片 */
.order-info-card {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.order-info-header {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #FFF0F3;

  .order-info-icon {
    font-size: 28rpx;
  }

  .order-info-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333333;
  }
}

.preview-item {
  display: flex;
  align-items: center;
  padding: 10rpx 0;

  .preview-emoji {
    font-size: 28rpx;
    margin-right: 12rpx;
  }

  .preview-name {
    flex: 1;
    font-size: 26rpx;
    color: #333333;
  }

  .preview-quantity {
    font-size: 24rpx;
    color: #999999;
  }
}

/* 评分区域 */
.rating-section {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 32rpx 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333333;
  display: block;
  margin-bottom: 24rpx;
}

/* 评价标签 */
.tags-section {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.tag-item {
  padding: 12rpx 28rpx;
  background: #FFF5F7;
  border-radius: 30rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s ease;

  &.active {
    background: #FFE4EC;
    border-color: #FF6B9D;

    .tag-text {
      color: #FF6B9D;
      font-weight: 600;
    }
  }

  &:active {
    transform: scale(0.95);
  }

  .tag-text {
    font-size: 24rpx;
    color: #666666;
  }
}

/* 评价内容 */
.content-section {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
}

.content-input {
  width: 100%;
  min-height: 200rpx;
  font-size: 28rpx;
  color: #333333;
  line-height: 1.6;
  padding: 16rpx;
  background: #FFF5F7;
  border-radius: 12rpx;
  box-sizing: border-box;
}

.input-placeholder {
  color: #cccccc;
  font-size: 26rpx;
}

.content-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16rpx;

  .char-count {
    font-size: 22rpx;
    color: #cccccc;
  }
}

.add-image-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 10rpx 20rpx;
  background: #FFF5F7;
  border-radius: 20rpx;

  &:active {
    background: #FFE4EC;
  }

  .add-image-icon {
    font-size: 24rpx;
  }

  .add-image-text {
    font-size: 22rpx;
    color: #FF6B9D;
  }
}

/* 匿名评价 */
.anonymous-section {
  background: #ffffff;
  border-radius: 20rpx;
  padding: 24rpx 28rpx;
  margin-bottom: 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);

  .anonymous-label {
    font-size: 28rpx;
    color: #333333;
  }
}

.anonymous-switch {
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
}

/* 提交按钮 */
.submit-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16rpx 24rpx;
  background: #ffffff;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.06);
  z-index: 100;
}

.submit-btn {
  padding: 24rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 50rpx;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(255, 107, 157, 0.4);

  &:active {
    transform: scale(0.97);
  }

  .submit-btn-text {
    font-size: 32rpx;
    color: #ffffff;
    font-weight: 600;
  }
}
</style>
