/**
 * 爱心点单 - 用户状态管理
 * 管理用户信息、token、爱心积分等
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { setToken, getToken, setUserInfo, getUserInfo, removeToken, removeUserInfo, isLoggedIn } from '../utils/auth.js'
import { post } from '../utils/request.js'

export const useUserStore = defineStore('user', () => {
  // ========== 状态 ==========
  /** 用户信息 */
  const userInfo = ref(getUserInfo() || {
    id: '',
    nickname: '',
    avatar: '',
    role: '', // boyfriend / girlfriend
    phone: '',
    gender: ''
  })

  /** 登录token */
  const token = ref(getToken())

  /** 爱心积分 */
  const lovePoints = ref(userInfo.value.lovePoints || 0)

  /** 是否已签到 */
  const isSigned = ref(false)

  /** 在一起的日期 */
  const loveStartDate = ref(uni.getStorageSync('loveStartDate') || '')

  /** 纪念日列表 */
  const anniversaries = ref(uni.getStorageSync('anniversaries') ? JSON.parse(uni.getStorageSync('anniversaries')) : [])

  // ========== 计算属性 ==========
  /** 是否已登录 */
  const loggedIn = computed(() => !!token.value)

  /** 角色描述 */
  const roleText = computed(() => {
    return userInfo.value.role === 'boyfriend' ? '男朋友' : '女朋友'
  })

  /** 角色emoji */
  const roleEmoji = computed(() => {
    return userInfo.value.role === 'boyfriend' ? '🤵' : '👰'
  })

  // ========== 方法 ==========

  /**
   * 检查登录状态
   */
  function checkLogin() {
    const t = getToken()
    const info = getUserInfo()
    if (t) {
      token.value = t
      if (info) {
        userInfo.value = info
        lovePoints.value = info.lovePoints || 0
      }
    }
  }

  /**
   * 设置用户信息
   */
  function setUserData(data) {
    userInfo.value = data
    token.value = data.token || token.value
    lovePoints.value = data.lovePoints || 0

    if (data.token) {
      setToken(data.token)
    }
    setUserInfo(data)
  }

  /**
   * 每日签到
   */
  async function dailySign() {
    if (isSigned.value) {
      uni.showToast({
        title: '今天已经签到过啦~',
        icon: 'none'
      })
      return false
    }

    try {
      // 调用后端签到接口，后端返回的是当前总积分
      const totalPoints = await post('/user/signin')
      
      // 直接使用后端返回的总积分
      lovePoints.value = totalPoints
      isSigned.value = true

      // 更新本地存储
      const info = getUserInfo()
      if (info) {
        info.lovePoints = totalPoints
        setUserInfo(info)
      }

      uni.showToast({
        title: `签到成功！当前积分：${totalPoints}`,
        icon: 'none',
        duration: 2000
      })

      return true
    } catch (error) {
      console.error('签到失败', error)
      uni.showToast({
        title: '签到失败，请重试',
        icon: 'none'
      })
      return false
    }
  }

  /**
   * 更新爱心积分
   */
  function updateLovePoints(points) {
    lovePoints.value = points
    const info = getUserInfo()
    if (info) {
      info.lovePoints = points
      setUserInfo(info)
    }
  }

  /**
   * 设置在一起日期
   */
  function setLoveStartDate(date) {
    loveStartDate.value = date
    uni.setStorageSync('loveStartDate', date)
  }

  /**
   * 添加纪念日
   */
  function addAnniversary(item) {
    anniversaries.value.push({
      id: Date.now().toString(),
      ...item,
      createdAt: new Date().toISOString()
    })
    uni.setStorageSync('anniversaries', JSON.stringify(anniversaries.value))
  }

  /**
   * 删除纪念日
   */
  function removeAnniversary(id) {
    anniversaries.value = anniversaries.value.filter(item => item.id !== id)
    uni.setStorageSync('anniversaries', JSON.stringify(anniversaries.value))
  }

  /**
   * 退出登录
   */
  function logout() {
    userInfo.value = {
      id: '',
      nickname: '',
      avatar: '',
      role: '',
      phone: '',
      gender: ''
    }
    token.value = ''
    lovePoints.value = 0
    isSigned.value = false
    removeToken()
    removeUserInfo()
  }

  return {
    // 状态
    userInfo,
    token,
    lovePoints,
    isSigned,
    loveStartDate,
    anniversaries,
    // 计算属性
    loggedIn,
    roleText,
    roleEmoji,
    // 方法
    checkLogin,
    setUserData,
    dailySign,
    updateLovePoints,
    setLoveStartDate,
    addAnniversary,
    removeAnniversary,
    logout
  }
})
