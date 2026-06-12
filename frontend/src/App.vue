<script>
/**
 * 爱心点单 - 应用入口
 */
export default {
  onLaunch() {
    console.log('App Launch - 爱心点单启动')
    
    // 检查登录状态
    const token = uni.getStorageSync('token')
    if (!token) {
      setTimeout(() => {
        uni.reLaunch({
          url: '/pages/login/login'
        })
      }, 500)
    }
  },
  
  onShow() {
    console.log('App Show')
  },
  
  onHide() {
    console.log('App Hide')
  }
}
</script>
<style lang="scss">
/* 全局样式 */
@import './uni.scss';

page {
  background-color: $bg-page;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC',
    'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  font-size: $font-base;
  color: $text-primary;
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
}

/* 全局滚动条美化 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  display: none;
}

/* 全局过渡动画 */
view, text, image {
  transition: all 0.3s ease;
}

/* 爱心跳动动画 */
@keyframes heartbeat {
  0% { transform: scale(1); }
  25% { transform: scale(1.1); }
  50% { transform: scale(1); }
  75% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

/* 爱心飞出动画 */
@keyframes heartFloat {
  0% {
    opacity: 1;
    transform: translateY(0) scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.8;
    transform: translateY(-60rpx) scale(1.2) rotate(-15deg);
  }
  100% {
    opacity: 0;
    transform: translateY(-120rpx) scale(0.6) rotate(15deg);
  }
}

/* 爱心飘落动画 */
@keyframes heartFall {
  0% {
    opacity: 0;
    transform: translateY(-20rpx) scale(0.5);
  }
  20% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
  100% {
    opacity: 0;
    transform: translateY(100rpx) scale(0.3);
  }
}

/* 渐入动画 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20rpx); }
  to { opacity: 1; transform: translateY(0); }
}

/* 缩放弹入动画 */
@keyframes scaleIn {
  from { opacity: 0; transform: scale(0.8); }
  to { opacity: 1; transform: scale(1); }
}

/* 从右滑入 */
@keyframes slideInRight {
  from { opacity: 0; transform: translateX(60rpx); }
  to { opacity: 1; transform: translateX(0); }
}

/* 从下滑入 */
@keyframes slideInUp {
  from { opacity: 0; transform: translateY(60rpx); }
  to { opacity: 1; transform: translateY(0); }
}

/* 闪烁动画 */
@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

/* 旋转动画 */
@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 脉冲动画 */
@keyframes pulse {
  0% { transform: scale(1); box-shadow: 0 0 0 0 rgba(255, 107, 157, 0.4); }
  70% { transform: scale(1.05); box-shadow: 0 0 0 10rpx rgba(255, 107, 157, 0); }
  100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(255, 107, 157, 0); }
}

/* 通用工具类 */
.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.text-ellipsis-2 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.safe-bottom {
  padding-bottom: env(safe-area-inset-bottom);
}

/* 隐藏原生导航栏时的占位 */
.status-bar {
  height: var(--status-bar-height);
  background: $gradient-primary;
}
</style>