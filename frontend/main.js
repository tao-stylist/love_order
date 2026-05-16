/**
 * 爱心点单 - 入口文件
 * 注册Pinia状态管理
 */
import { createSSRApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'

export function createApp() {
  const app = createSSRApp(App)

  // 创建Pinia实例
  const pinia = createPinia()
  app.use(pinia)

  return {
    app,
    pinia
  }
}
