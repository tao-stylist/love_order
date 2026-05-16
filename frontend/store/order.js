/**
 * 爱心点单 - 订单状态管理
 * 管理购物车、订单列表、菜品数据等
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useOrderStore = defineStore('order', () => {
  // ========== 购物车状态 ==========
  /** 购物车列表 [{ menuId, name, price, image, quantity, desc }] */
  const cartList = ref([])

  /** 当前选中的分类 */
  const currentCategory = ref('推荐')

  /** 搜索关键词 */
  const searchKeyword = ref('')

  // ========== 订单状态 ==========
  /** 订单列表 */
  const orderList = ref([])

  /** 当前订单详情 */
  const currentOrder = ref(null)

  /** 订单加载状态 */
  const orderLoading = ref(false)

  /** 订单页码 */
  const orderPage = ref(1)

  /** 订单是否还有更多 */
  const orderHasMore = ref(true)

  /** 当前订单tab */
  const orderTab = ref('all')

  // ========== 菜品数据 ==========
  /** 菜品分类列表 */
  const categories = ref([
    { id: 'recommend', name: '推荐', icon: '🌟' },
    { id: 'staple', name: '主食', icon: '🍚' },
    { id: 'snack', name: '小吃', icon: '🍟' },
    { id: 'drink', name: '饮品', icon: '🧋' },
    { id: 'dessert', name: '甜品', icon: '🍰' },
    { id: 'soup', name: '汤品', icon: '🍲' },
    { id: 'set', name: '套餐', icon: '🍱' }
  ])

  /** 菜品列表（模拟数据） */
  const menuList = ref([
    // 推荐菜品
    {
      id: '1', name: '爱心便当', price: 28.8, originalPrice: 35,
      image: '', category: 'recommend', desc: '满满都是爱意的便当~',
      tags: ['热销', '推荐'], sales: 520
    },
    {
      id: '2', name: '甜蜜双人餐', price: 66.6, originalPrice: 88,
      image: '', category: 'recommend', desc: '和TA一起享用的甜蜜套餐',
      tags: ['双人', '超值'], sales: 366
    },
    {
      id: '3', name: '暖心情人汤', price: 18.8, originalPrice: 25,
      image: '', category: 'recommend', desc: '暖胃又暖心的好汤',
      tags: ['暖胃'], sales: 288
    },
    // 主食
    {
      id: '4', name: '番茄鸡蛋面', price: 15.8, originalPrice: 20,
      image: '', category: 'staple', desc: '家常味道，满满幸福',
      tags: ['家常'], sales: 456
    },
    {
      id: '5', name: '咖喱鸡饭', price: 22.8, originalPrice: 28,
      image: '', category: 'staple', desc: '浓郁咖喱，香糯米饭',
      tags: ['人气'], sales: 388
    },
    {
      id: '6', name: '红烧牛肉面', price: 19.8, originalPrice: 25,
      image: '', category: 'staple', desc: '大块牛肉，劲道面条',
      tags: ['招牌'], sales: 666
    },
    // 小吃
    {
      id: '7', name: '炸鸡翅', price: 12.8, originalPrice: 16,
      image: '', category: 'snack', desc: '外酥里嫩，香气四溢',
      tags: ['热卖'], sales: 588
    },
    {
      id: '8', name: '薯条', price: 8.8, originalPrice: 12,
      image: '', category: 'snack', desc: '金黄酥脆，蘸酱更美味',
      tags: [], sales: 432
    },
    // 饮品
    {
      id: '9', name: '珍珠奶茶', price: 13.8, originalPrice: 18,
      image: '', category: 'drink', desc: 'Q弹珍珠，丝滑奶茶',
      tags: ['必喝'], sales: 888
    },
    {
      id: '10', name: '草莓奶昔', price: 16.8, originalPrice: 22,
      image: '', category: 'drink', desc: '新鲜草莓，甜蜜奶昔',
      tags: ['季节限定'], sales: 321
    },
    {
      id: '11', name: '柠檬蜂蜜水', price: 9.8, originalPrice: 12,
      image: '', category: 'drink', desc: '清新柠檬，甜蜜蜂蜜',
      tags: ['清爽'], sales: 567
    },
    // 甜品
    {
      id: '12', name: '提拉米苏', price: 22.8, originalPrice: 28,
      image: '', category: 'dessert', desc: '经典意式甜品，入口即化',
      tags: ['人气'], sales: 234
    },
    {
      id: '13', name: '芒果布丁', price: 12.8, originalPrice: 16,
      image: '', category: 'dessert', desc: '新鲜芒果，嫩滑布丁',
      tags: ['清爽'], sales: 345
    },
    // 汤品
    {
      id: '14', name: '紫菜蛋花汤', price: 6.8, originalPrice: 8,
      image: '', category: 'soup', desc: '清淡鲜美，暖胃首选',
      tags: [], sales: 678
    },
    // 套餐
    {
      id: '15', name: '情侣套餐A', price: 88.8, originalPrice: 128,
      image: '', category: 'set', desc: '两份主食+两份饮品+一份甜品',
      tags: ['超值', '情侣'], sales: 199
    },
    {
      id: '16', name: '单人豪华餐', price: 38.8, originalPrice: 52,
      image: '', category: 'set', desc: '主食+小吃+饮品，一个人也要吃好',
      tags: ['超值'], sales: 267
    }
  ])

  // ========== 购物车计算属性 ==========
  /** 购物车商品总数 */
  const cartTotalCount = computed(() => {
    return cartList.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  /** 购物车总价 */
  const cartTotalPrice = computed(() => {
    return cartList.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  })

  /** 购物车是否为空 */
  const cartEmpty = computed(() => {
    return cartList.value.length === 0
  })

  /** 按分类筛选的菜品 */
  const filteredMenuList = computed(() => {
    let list = menuList.value
    // 按分类筛选
    if (currentCategory.value && currentCategory.value !== '推荐') {
      list = list.filter(item => item.category === currentCategory.value)
    }
    // 按关键词搜索
    if (searchKeyword.value) {
      const keyword = searchKeyword.value.toLowerCase()
      list = list.filter(item =>
        item.name.toLowerCase().includes(keyword) ||
        item.desc.toLowerCase().includes(keyword)
      )
    }
    return list
  })

  /** 推荐菜品（用于首页） */
  const recommendList = computed(() => {
    return menuList.value.filter(item => item.category === 'recommend')
  })

  // ========== 购物车方法 ==========

  /**
   * 添加到购物车
   */
  function addToCart(item) {
    const existItem = cartList.value.find(cart => cart.menuId === item.id)
    if (existItem) {
      existItem.quantity++
    } else {
      cartList.value.push({
        menuId: item.id,
        name: item.name,
        price: item.price,
        image: item.image,
        quantity: 1,
        desc: item.desc
      })
    }

    uni.showToast({
      title: `${item.name} 已加入购物车`,
      icon: 'success',
      duration: 1500
    })
  }

  /**
   * 减少购物车商品数量
   */
  function decreaseCartItem(menuId) {
    const index = cartList.value.findIndex(item => item.menuId === menuId)
    if (index > -1) {
      if (cartList.value[index].quantity > 1) {
        cartList.value[index].quantity--
      } else {
        cartList.value.splice(index, 1)
      }
    }
  }

  /**
   * 增加购物车商品数量
   */
  function increaseCartItem(menuId) {
    const item = cartList.value.find(item => item.menuId === menuId)
    if (item) {
      item.quantity++
    }
  }

  /**
   * 移除购物车商品
   */
  function removeFromCart(menuId) {
    const index = cartList.value.findIndex(item => item.menuId === menuId)
    if (index > -1) {
      cartList.value.splice(index, 1)
    }
  }

  /**
   * 清空购物车
   */
  function clearCart() {
    cartList.value = []
  }

  // ========== 订单方法 ==========

  /**
   * 提交订单（模拟）
   */
  function submitOrder(orderData) {
    const order = {
      id: 'ORD' + Date.now().toString().slice(-10),
      orderNo: 'LO' + Date.now().toString().slice(-8),
      items: [...cartList.value],
      totalPrice: cartTotalPrice.value,
      status: 'pending', // pending/accepted/making/delivering/completed
      statusText: '待接单',
      createTime: new Date().toISOString(),
      ...orderData
    }

    // 添加到订单列表头部
    orderList.value.unshift(order)
    // 清空购物车
    clearCart()

    return order
  }

  /**
   * 获取订单列表（模拟）
   */
  function getOrders(tab = 'all', refresh = false) {
    if (refresh) {
      orderPage.value = 1
      orderHasMore.value = true
    }

    orderLoading.value = true

    // 模拟加载
    setTimeout(() => {
      // 模拟订单数据
      if (orderList.value.length === 0) {
        const mockOrders = [
          {
            id: '1',
            orderNo: 'LO20240516001',
            items: [
              { menuId: '1', name: '爱心便当', price: 28.8, quantity: 1 },
              { menuId: '9', name: '珍珠奶茶', price: 13.8, quantity: 2 }
            ],
            totalPrice: 56.4,
            status: 'completed',
            statusText: '已完成',
            createTime: '2024-05-15T12:30:00',
            remark: '少放辣哦~'
          },
          {
            id: '2',
            orderNo: 'LO20240516002',
            items: [
              { menuId: '15', name: '情侣套餐A', price: 88.8, quantity: 1 }
            ],
            totalPrice: 88.8,
            status: 'delivering',
            statusText: '配送中',
            createTime: '2024-05-16T11:00:00',
            remark: '送到楼下'
          },
          {
            id: '3',
            orderNo: 'LO20240516003',
            items: [
              { menuId: '6', name: '红烧牛肉面', price: 19.8, quantity: 1 },
              { menuId: '7', name: '炸鸡翅', price: 12.8, quantity: 1 }
            ],
            totalPrice: 32.6,
            status: 'making',
            statusText: '制作中',
            createTime: '2024-05-16T11:30:00',
            remark: ''
          },
          {
            id: '4',
            orderNo: 'LO20240516004',
            items: [
              { menuId: '12', name: '提拉米苏', price: 22.8, quantity: 1 },
              { menuId: '10', name: '草莓奶昔', price: 16.8, quantity: 1 }
            ],
            totalPrice: 39.6,
            status: 'pending',
            statusText: '待接单',
            createTime: '2024-05-16T12:00:00',
            remark: '生日快乐！'
          }
        ]
        orderList.value = mockOrders
      }

      orderLoading.value = false
    }, 800)
  }

  /**
   * 获取订单详情
   */
  function getOrderDetail(orderId) {
    return orderList.value.find(order => order.id === orderId) || null
  }

  /**
   * 设置当前订单
   */
  function setCurrentOrder(order) {
    currentOrder.value = order
  }

  return {
    // 购物车状态
    cartList,
    currentCategory,
    searchKeyword,
    // 订单状态
    orderList,
    currentOrder,
    orderLoading,
    orderPage,
    orderHasMore,
    orderTab,
    // 菜品数据
    categories,
    menuList,
    // 计算属性
    cartTotalCount,
    cartTotalPrice,
    cartEmpty,
    filteredMenuList,
    recommendList,
    // 购物车方法
    addToCart,
    decreaseCartItem,
    increaseCartItem,
    removeFromCart,
    clearCart,
    // 订单方法
    submitOrder,
    getOrders,
    getOrderDetail,
    setCurrentOrder
  }
})
