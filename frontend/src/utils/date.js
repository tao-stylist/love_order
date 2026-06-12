/**
 * 爱心点单 - 日期工具函数
 * 日期格式化、纪念日倒计时计算
 */

/**
 * 格式化日期
 * @param {Date|String|Number} date 日期对象/时间戳/日期字符串
 * @param {String} format 格式化模板
 * @returns {String} 格式化后的日期字符串
 */
export function formatDate(date, format = 'YYYY-MM-DD') {
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  const formatMap = {
    'YYYY': year,
    'MM': month,
    'DD': day,
    'HH': hours,
    'mm': minutes,
    'ss': seconds,
    'M': d.getMonth() + 1,
    'D': d.getDate(),
    'H': d.getHours(),
    'm': d.getMinutes(),
    's': d.getSeconds()
  }

  let result = format
  // 按长度降序替换，避免 MM 被 M 先替换
  const keys = Object.keys(formatMap).sort((a, b) => b.length - a.length)
  keys.forEach(key => {
    result = result.replace(new RegExp(key, 'g'), formatMap[key])
  })

  return result
}

/**
 * 获取相对时间描述
 * 如：刚刚、5分钟前、1小时前、昨天、3天前
 */
export function getTimeAgo(date) {
  const now = new Date()
  const d = new Date(date)
  const diff = now.getTime() - d.getTime()
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (seconds < 60) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`

  return formatDate(date, 'MM-DD HH:mm')
}

/**
 * 计算纪念日倒计时天数
 * @param {String|Date} date 纪念日日期
 * @returns {Object} { days: 距离天数, isToday: 是否今天, isPast: 是否已过 }
 */
export function getAnniversaryCountdown(date) {
  const now = new Date()
  const target = new Date(date)
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const anniversary = new Date(today.getFullYear(), target.getMonth(), target.getDate())

  // 如果今年的纪念日已过，算到明年
  if (anniversary < today) {
    anniversary.setFullYear(anniversary.getFullYear() + 1)
  }

  const diffTime = anniversary.getTime() - today.getTime()
  const days = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

  return {
    days,
    isToday: days === 0,
    isPast: false,
    date: formatDate(anniversary, 'YYYY-MM-DD')
  }
}

/**
 * 计算在一起多少天
 * @param {String|Date} startDate 在一起的日期
 * @returns {Number} 天数
 */
export function getDaysTogether(startDate) {
  const start = new Date(startDate)
  const now = new Date()
  const diffTime = now.getTime() - start.getTime()
  return Math.floor(diffTime / (1000 * 60 * 60 * 24))
}

/**
 * 获取星期几的中文
 */
export function getWeekDay(date) {
  const days = ['日', '一', '二', '三', '四', '五', '六']
  return `星期${days[new Date(date).getDay()]}`
}

/**
 * 获取日期的友好显示
 * 如：今天 14:30、明天 09:00、后天 15:00、12月25日 10:00
 */
export function getFriendlyDate(date) {
  const now = new Date()
  const d = new Date(date)
  const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  const targetStart = new Date(d.getFullYear(), d.getMonth(), d.getDate())
  const diffDays = Math.floor((targetStart - todayStart) / (1000 * 60 * 60 * 24))
  const timeStr = formatDate(date, 'HH:mm')

  if (diffDays === 0) return `今天 ${timeStr}`
  if (diffDays === 1) return `明天 ${timeStr}`
  if (diffDays === 2) return `后天 ${timeStr}`

  return `${formatDate(date, 'MM月DD日')} ${timeStr}`
}

/**
 * 生成可选的送达时间列表
 * 从当前时间开始，每30分钟一个选项，生成未来3小时的时间
 */
export function getDeliveryTimeOptions() {
  const now = new Date()
  const options = []

  // 取整到下一个30分钟
  const minutes = now.getMinutes()
  const offset = minutes < 30 ? 30 - minutes : 60 - minutes
  const startTime = new Date(now.getTime() + offset * 60 * 1000)

  for (let i = 0; i < 6; i++) {
    const time = new Date(startTime.getTime() + i * 30 * 60 * 1000)
    options.push({
      label: getFriendlyDate(time),
      value: formatDate(time, 'YYYY-MM-DD HH:mm:ss')
    })
  }

  return options
}

/**
 * 判断是否是特殊纪念日
 * 检查是否是情人节、七夕、生日等
 */
export function checkSpecialDay(date) {
  const d = new Date(date)
  const month = d.getMonth() + 1
  const day = d.getDate()

  const specialDays = [
    { month: 2, day: 14, name: '情人节', emoji: '💕' },
    { month: 3, day: 14, name: '白色情人节', emoji: '🤍' },
    { month: 5, day: 20, name: '520', emoji: '💗' },
    { month: 7, day: 7, name: '七夕', emoji: '🌙' },
    { month: 8, day: 7, name: '七夕', emoji: '🌙' }, // 农历七夕近似
    { month: 11, day: 11, name: '光棍节', emoji: '🎁' },
    { month: 12, day: 24, name: '平安夜', emoji: '🎄' },
    { month: 12, day: 25, name: '圣诞节', emoji: '🎅' }
  ]

  return specialDays.find(item => item.month === month && item.day === day) || null
}

export default {
  formatDate,
  getTimeAgo,
  getAnniversaryCountdown,
  getDaysTogether,
  getWeekDay,
  getFriendlyDate,
  getDeliveryTimeOptions,
  checkSpecialDay
}
