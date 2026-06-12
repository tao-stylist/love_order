/**
 * 爱心点单 - 认证与用户信息管理工具
 * 处理登录、token存储、用户信息管理
 */
import { post } from './request.js'

// Token存储key
const TOKEN_KEY = 'token'
const USER_INFO_KEY = 'userInfo'
const REFRESH_TOKEN_KEY = 'refreshToken'

/**
 * 保存Token
 */
export function setToken(token) {
  uni.setStorageSync(TOKEN_KEY, token)
}

/**
 * 获取Token
 */
export function getToken() {
  return uni.getStorageSync(TOKEN_KEY) || ''
}

/**
 * 移除Token
 */
export function removeToken() {
  uni.removeStorageSync(TOKEN_KEY)
  uni.removeStorageSync(REFRESH_TOKEN_KEY)
}

/**
 * 保存用户信息
 */
export function setUserInfo(userInfo) {
  uni.setStorageSync(USER_INFO_KEY, JSON.stringify(userInfo))
}

/**
 * 获取用户信息
 */
export function getUserInfo() {
  try {
    const info = uni.getStorageSync(USER_INFO_KEY)
    return info ? JSON.parse(info) : null
  } catch (e) {
    return null
  }
}

/**
 * 移除用户信息
 */
export function removeUserInfo() {
  uni.removeStorageSync(USER_INFO_KEY)
}

/**
 * 判断是否已登录
 */
export function isLoggedIn() {
  return !!getToken()
}

/**
 * 微信小程序登录
 * 获取微信code后发送到后端换取token
 */
export async function wxLogin(role = 'boyfriend') {
  return new Promise((resolve, reject) => {
    // #ifdef MP-WEIXIN
    uni.login({
      provider: 'weixin',
      success: async (loginRes) => {
        try {
          const res = await post('/auth/wx-login', {
            code: loginRes.code,
            role: role
          })
          // 保存token和用户信息
          setToken(res.token)
          setUserInfo(res.userInfo)
          resolve(res)
        } catch (error) {
          reject(error)
        }
      },
      fail: (error) => {
        uni.showToast({
          title: '微信登录失败',
          icon: 'none'
        })
        reject(error)
      }
    })
    // #endif

    // #ifdef H5
    // H5环境调用真实后端登录接口
    setTimeout(async () => {
      try {
        // 使用 openid + 昵称/头像模拟微信登录
        const mockOpenid = 'h5_test_openid_' + Date.now()
        const mockNickname = role === 'boyfriend' ? '暖男小王' : '甜心小美'
        
        const res = await post('/user/login', {
          openid: mockOpenid,
          nickname: mockNickname,
          avatar: ''
        })
        
        // 保存token和用户信息
        setToken(res.token)
        
        const userInfo = {
          id: res.userId,
          nickname: res.nickname,
          avatar: res.avatar,
          role: role,
          lovePoints: res.lovePoints
        }
        setUserInfo(userInfo)
        
        resolve({
          token: res.token,
          userInfo: userInfo
        })
      } catch (error) {
        console.error('登录失败', error)
        reject(error)
      }
    }, 500)
    // #endif
  })
}

/**
 * H5手机号登录
 */
export async function phoneLogin(phone, nickname) {
  try {
    const res = await post('/auth/phone-login', { phone, nickname })
    // 保存token和用户信息
    setToken(res.token)
    
    const userInfo = {
      id: res.userId,
      nickname: res.nickname,
      avatar: res.avatar,
      lovePoints: res.lovePoints,
      phone: res.phone,
      bindCode: res.bindCode,
      partnerId: res.partnerId
    }
    setUserInfo(userInfo)
    
    return {
      token: res.token,
      userInfo
    }
  } catch (error) {
    throw error
  }
}

/**
 * 退出登录
 */
export function logout() {
  removeToken()
  removeUserInfo()

  uni.showToast({
    title: '已退出登录',
    icon: 'none'
  })

  setTimeout(() => {
    uni.reLaunch({
      url: '/pages/login/login'
    })
  }, 1000)
}

/**
 * 更新用户信息
 */
export async function updateUserInfo(data) {
  try {
    const res = await post('/user/update', data)
    // 更新本地缓存
    const currentInfo = getUserInfo()
    const newInfo = { ...currentInfo, ...res }
    setUserInfo(newInfo)
    return newInfo
  } catch (error) {
    throw error
  }
}

/**
 * 获取手机号（微信小程序）
 */
export function getPhoneNumber(e) {
  return new Promise((resolve, reject) => {
    if (e.detail.errMsg === 'getPhoneNumber:ok') {
      // 发送加密数据到后端解密
      post('/auth/decrypt-phone', {
        encryptedData: e.detail.encryptedData,
        iv: e.detail.iv
      }).then(resolve).catch(reject)
    } else {
      reject(new Error('用户拒绝授权'))
    }
  })
}

export default {
  setToken,
  getToken,
  removeToken,
  setUserInfo,
  getUserInfo,
  removeUserInfo,
  isLoggedIn,
  wxLogin,
  phoneLogin,
  logout,
  updateUserInfo,
  getPhoneNumber
}
