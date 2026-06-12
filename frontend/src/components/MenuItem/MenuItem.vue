<template>
  <!-- 菜品卡片组件 -->
  <view class="menu-item" @click="handleDetail">
    <!-- 菜品图片区域 -->
    <view class="menu-item-image">
      <view class="image-placeholder" :style="{ background: placeholderBg }">
        <text class="placeholder-emoji">{{ emoji }}</text>
      </view>
      <!-- 标签 -->
      <view v-if="item.tags && item.tags.length" class="menu-item-tags">
        <text
          v-for="(tag, idx) in item.tags.slice(0, 2)"
          :key="idx"
          class="tag"
        >{{ tag }}</text>
      </view>
      <!-- 折扣角标 -->
      <view v-if="item.originalPrice && item.originalPrice > item.price" class="discount-badge">
        <text>省</text>
      </view>
    </view>

    <!-- 菜品信息 -->
    <view class="menu-item-info">
      <text class="menu-item-name">{{ item.name }}</text>
      <text class="menu-item-desc">{{ item.desc }}</text>
      <view class="menu-item-bottom">
        <view class="menu-item-price">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ item.price }}</text>
          <text v-if="item.originalPrice" class="price-original">¥{{ item.originalPrice }}</text>
        </view>
        <!-- 月售 -->
        <text class="menu-item-sales">月售{{ item.sales }}</text>
      </view>
      <!-- 加入购物车按钮 -->
      <view class="add-cart-btn" @click.stop="handleAddCart">
        <text class="add-icon">+</text>
      </view>
    </view>

    <!-- 爱心飞出动画 -->
    <view v-if="showHeart" class="heart-animation">
      <text class="heart-emoji">❤️</text>
    </view>
  </view>
</template>

<script setup>
/**
 * 菜品卡片组件
 * 展示菜品图片、名称、描述、价格，支持加入购物车
 */
import { ref, computed } from 'vue'

const props = defineProps({
  /** 菜品数据 */
  item: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['add-cart', 'detail'])

// 爱心动画状态
const showHeart = ref(false)

// 根据分类生成不同的emoji
const emoji = computed(() => {
  const emojiMap = {
    'recommend': '💝',
    'staple': '🍱',
    'snack': '🍟',
    'drink': '🧋',
    'dessert': '🍰',
    'soup': '🍲',
    'set': '🍱'
  }
  return emojiMap[props.item.category] || '🍽️'
})

// 占位图背景色
const placeholderBg = computed(() => {
  const colors = [
    'linear-gradient(135deg, #FFE4EC, #FFF0F3)',
    'linear-gradient(135deg, #FFF0E6, #FFF8F0)',
    'linear-gradient(135deg, #E8F5E9, #F1F8E9)',
    'linear-gradient(135deg, #E3F2FD, #F3F9FF)',
    'linear-gradient(135deg, #F3E5F5, #FCE4EC)'
  ]
  return colors[props.item.id % colors.length]
})

/**
 * 加入购物车
 */
function handleAddCart() {
  showHeart.value = true
  setTimeout(() => {
    showHeart.value = false
  }, 800)

  emit('add-cart', props.item)
}

/**
 * 查看详情
 */
function handleDetail() {
  emit('detail', props.item)
}
</script>

<style lang="scss" scoped>
.menu-item {
  display: flex;
  padding: 24rpx;
  background: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(255, 107, 157, 0.06);
  position: relative;
  overflow: visible;
  animation: fadeIn 0.4s ease;
}

.menu-item-image {
  position: relative;
  width: 180rpx;
  height: 180rpx;
  border-radius: 16rpx;
  overflow: hidden;
  flex-shrink: 0;
  margin-right: 20rpx;

  .image-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;

    .placeholder-emoji {
      font-size: 72rpx;
    }
  }
}

.menu-item-tags {
  position: absolute;
  top: 8rpx;
  left: 8rpx;
  display: flex;
  gap: 6rpx;

  .tag {
    font-size: 18rpx;
    color: #ffffff;
    background: linear-gradient(135deg, #FF6B9D, #FFA07A);
    padding: 2rpx 10rpx;
    border-radius: 20rpx;
  }
}

.discount-badge {
  position: absolute;
  top: 0;
  right: 0;
  width: 36rpx;
  height: 36rpx;
  background: #FF4757;
  color: #ffffff;
  font-size: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 16rpx 0 8rpx;
}

.menu-item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  min-width: 0;
  position: relative;
}

.menu-item-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.menu-item-desc {
  font-size: 22rpx;
  color: #999999;
  margin-top: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.menu-item-bottom {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-top: 12rpx;
}

.menu-item-price {
  display: flex;
  align-items: baseline;

  .price-symbol {
    font-size: 22rpx;
    color: #FF4757;
    font-weight: 600;
  }

  .price-value {
    font-size: 36rpx;
    color: #FF4757;
    font-weight: 700;
    margin-left: 2rpx;
  }

  .price-original {
    font-size: 20rpx;
    color: #cccccc;
    text-decoration: line-through;
    margin-left: 8rpx;
  }
}

.menu-item-sales {
  font-size: 20rpx;
  color: #bbbbbb;
}

.add-cart-btn {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 52rpx;
  height: 52rpx;
  background: linear-gradient(135deg, #FF6B9D, #FFA07A);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(255, 107, 157, 0.3);

  .add-icon {
    font-size: 36rpx;
    color: #ffffff;
    font-weight: 300;
    line-height: 1;
    margin-top: -2rpx;
  }

  &:active {
    transform: scale(0.9);
  }
}

// 爱心飞出动画
.heart-animation {
  position: absolute;
  right: 20rpx;
  bottom: 60rpx;
  z-index: 10;
  pointer-events: none;

  .heart-emoji {
    font-size: 40rpx;
    animation: heartFloat 0.8s ease-out forwards;
  }
}
</style>
