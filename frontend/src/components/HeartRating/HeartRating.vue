<template>
  <!-- 爱心评分组件（用爱心代替星星） -->
  <view class="heart-rating">
    <view class="hearts-container">
      <view
        v-for="index in maxCount"
        :key="index"
        class="heart-item"
        :class="{ active: index <= currentValue, half: index === currentValue + 0.5 }"
        @click="handleClick(index)"
        @touchmove="handleTouchMove"
      >
        <text class="heart-icon">{{ index <= currentValue ? '❤️' : '🤍' }}</text>
      </view>
    </view>
    <!-- 评分文字 -->
    <text v-if="showText" class="rating-text">{{ ratingText }}</text>
  </view>
</template>

<script setup>
/**
 * 爱心评分组件
 * 用爱心代替传统星星评分，更符合甜蜜主题
 */
import { ref, computed, watch } from 'vue'

const props = defineProps({
  /** 当前评分值 */
  modelValue: {
    type: Number,
    default: 0
  },
  /** 最大评分数 */
  maxCount: {
    type: Number,
    default: 5
  },
  /** 是否显示评分文字 */
  showText: {
    type: Boolean,
    default: true
  },
  /** 是否只读 */
  readonly: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

// 当前选中值
const currentValue = ref(props.modelValue)

// 监听外部值变化
watch(() => props.modelValue, (val) => {
  currentValue.value = val
})

// 评分对应文字
const ratingText = computed(() => {
  const texts = ['', '不太满意', '一般般', '还不错', '很喜欢', '超级满意']
  return texts[Math.floor(currentValue.value)] || ''
})

/**
 * 点击评分
 */
function handleClick(index) {
  if (props.readonly) return

  // 如果点击已选中的值，取消选择
  if (currentValue.value === index) {
    currentValue.value = index - 1
  } else {
    currentValue.value = index
  }

  emit('update:modelValue', currentValue.value)
  emit('change', currentValue.value)
}

/**
 * 触摸滑动评分
 */
function handleTouchMove(e) {
  if (props.readonly) return
  // 简单处理，实际可以根据触摸位置计算
}
</script>

<style lang="scss" scoped>
.heart-rating {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.hearts-container {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.heart-item {
  cursor: pointer;
  transition: transform 0.2s cubic-bezier(0.68, -0.55, 0.265, 1.55);

  &:active {
    transform: scale(0.85);
  }

  &.active {
    .heart-icon {
      animation: heartbeat 0.6s ease;
    }
  }

  .heart-icon {
    font-size: 44rpx;
    transition: transform 0.2s ease;
  }
}

.rating-text {
  font-size: 24rpx;
  color: #FF6B9D;
  font-weight: 500;
  margin-left: 8rpx;
}
</style>
