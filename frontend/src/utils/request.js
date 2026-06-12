/**
 * 爱心点单 - 网络请求封装
 * 基于uni.request封装，支持拦截器、token管理
 */

// 基础请求地址
const BASE_URL = 'http://localhost:8081/api'

// 请求超时时间
const TIMEOUT = 15000

// 不需要token的接口白名单
const WHITE_LIST = [
  '/auth/login',
  '/auth/wx-login',
  '/user/login',
  '/menu/list',
  '/menu/detail',
  '/menu/recommend'
]

/**
 * 获取token
 */
function getToken() {
  return uni.getStorageSync('token') || ''
}

/**
 * 请求拦截器
 * 在请求发送前统一处理
 */
function requestInterceptor(config) {
  // 添加token
  const token = getToken()
  if (token && !WHITE_LIST.some(url => config.url.includes(url))) {
    config.header = config.header || {}
    config.header['Authorization'] = `Bearer ${token}`
  }

  // 添加通用header
  config.header = config.header || {}
  config.header['Content-Type'] = config.header['Content-Type'] || 'application/json'

  // 显示加载提示
  if (config.showLoading !== false) {
    uni.showLoading({
      title: config.loadingText || '加载中...',
      mask: true
    })
  }

  return config
}

/**
 * 响应拦截器
 * 统一处理响应数据
 */
function responseInterceptor(response) {
  // 隐藏加载提示
  uni.hideLoading()

  const { statusCode, data } = response

  // HTTP状态码检查
  if (statusCode === 200) {
    // 业务状态码检查
    if (data.code === 0 || data.code === 200) {
      return data.data
    }

    // token过期
    if (data.code === 401) {
      handleTokenExpired()
      return Promise.reject(new Error('登录已过期，请重新登录'))
    }

    // 业务错误
    const errMsg = data.message || '请求失败'
    uni.showToast({
      title: errMsg,
      icon: 'none',
      duration: 2000
    })
    return Promise.reject(new Error(errMsg))
  }

  // HTTP错误处理
  if (statusCode === 401) {
    handleTokenExpired()
    return Promise.reject(new Error('登录已过期'))
  }

  if (statusCode === 403) {
    uni.showToast({
      title: '没有权限访问',
      icon: 'none'
    })
    return Promise.reject(new Error('没有权限'))
  }

  if (statusCode === 404) {
    uni.showToast({
      title: '请求的资源不存在',
      icon: 'none'
    })
    return Promise.reject(new Error('资源不存在'))
  }

  if (statusCode >= 500) {
    uni.showToast({
      title: '服务器开小差了~',
      icon: 'none'
    })
    return Promise.reject(new Error('服务器错误'))
  }

  return Promise.reject(new Error('网络请求失败'))
}

/**
 * token过期处理
 */
function handleTokenExpired() {
  // 清除token
  uni.removeStorageSync('token')
  uni.removeStorageSync('userInfo')

  // 跳转到登录页
  setTimeout(() => {
    uni.reLaunch({
      url: '/pages/login/login'
    })
  }, 1500)

  uni.showToast({
    title: '登录已过期，请重新登录',
    icon: 'none',
    duration: 1500
  })
}

/**
 * 核心请求方法
 * @param {Object} options 请求配置
 * @returns {Promise} 请求结果
 */
function request(options) {
  // 合并配置
  const config = {
    url: BASE_URL + options.url,
    method: options.method || 'GET',
    data: options.data || {},
    header: options.header || {},
    timeout: options.timeout || TIMEOUT,
    showLoading: options.showLoading,
    loadingText: options.loadingText
  }

  // 请求拦截
  const finalConfig = requestInterceptor(config)

  return new Promise((resolve, reject) => {
    uni.request({
      ...finalConfig,
      success: (response) => {
        responseInterceptor(response)
          .then(resolve)
          .catch(reject)
      },
      fail: (error) => {
        uni.hideLoading()

        // 网络错误处理
        if (error.errMsg.includes('timeout')) {
          uni.showToast({
            title: '请求超时，请重试',
            icon: 'none'
          })
        } else if (error.errMsg.includes('fail')) {
          uni.showToast({
            title: '网络连接失败',
            icon: 'none'
          })
        }

        reject(error)
      }
    })
  })
}

/**
 * GET请求
 */
export function get(url, data = {}, options = {}) {
  return request({
    url,
    method: 'GET',
    data,
    ...options
  })
}

/**
 * POST请求
 */
export function post(url, data = {}, options = {}) {
  return request({
    url,
    method: 'POST',
    data,
    ...options
  })
}

/**
 * PUT请求
 */
export function put(url, data = {}, options = {}) {
  return request({
    url,
    method: 'PUT',
    data,
    ...options
  })
}

/**
 * DELETE请求
 */
export function del(url, data = {}, options = {}) {
  return request({
    url,
    method: 'DELETE',
    data,
    ...options
  })
}

/**
 * 上传文件
 */
export function upload(url, filePath, formData = {}) {
  const token = getToken()

  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: BASE_URL + url,
      filePath,
      name: 'file',
      formData,
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          const data = JSON.parse(res.data)
          if (data.code === 0 || data.code === 200) {
            resolve(data.data)
          } else {
            reject(new Error(data.message || '上传失败'))
          }
        } else {
          reject(new Error('上传失败'))
        }
      },
      fail: (error) => {
        reject(error)
      }
    })
  })
}

export default {
  get,
  post,
  put,
  del,
  upload,
  BASE_URL
}
