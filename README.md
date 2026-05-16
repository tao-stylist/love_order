# 💕 Love Order - 男朋友给女朋友的点单小程序

> 一个充满爱意的点单小程序，让男朋友可以随时为女朋友下单美食，记录每一个甜蜜瞬间。

## 📋 项目概述

| 项目 | 说明 |
|------|------|
| 项目名称 | Love Order（爱意点单） |
| 前端技术 | UniApp + Vue3 + Pinia + SCSS |
| 后端技术 | SpringBoot 3.2 + Spring Data JPA + Spring Security + JWT |
| 数据库 | MySQL 8.0 + Redis |
| 运行平台 | 微信小程序 + H5 |
| UI风格 | 可爱甜美粉色系 |

## 🎨 主题色系

| 用途 | 色值 | 预览 |
|------|------|------|
| 主色 | `#FF6B9D` | 💗 粉色 |
| 辅色 | `#FFA07A` | 🧡 浅珊瑚 |
| 强调色 | `#FF4757` | ❤️ 红色 |
| 背景色 | `#FFF5F7` | 🩷 淡粉 |
| 渐变 | `#FF6B9D → #FFA07A` | 🌅 粉色渐变 |

## 🚀 快速开始

### 环境要求

- **JDK**: 17+
- **Node.js**: 16+
- **MySQL**: 8.0+
- **Redis**: 6.0+
- **微信开发者工具**（小程序开发）

### 1. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE love_order_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 导入初始化脚本
USE love_order_db;
SOURCE backend/src/main/resources/init.sql;
```

### 2. 后端启动

```bash
cd backend

# 修改 application.yml 中的数据库和Redis连接信息
# vi src/main/resources/application.yml

# 启动后端服务
mvn spring-boot:run
```

后端服务运行在 `http://localhost:8080`

### 3. 前端启动

```bash
cd frontend

# 安装依赖
npm install

# H5模式运行
npm run dev:h5

# 微信小程序模式运行
npm run dev:mp-weixin
```

微信小程序需在微信开发者工具中导入 `frontend/dist/dev/mp-weixin` 目录。

## 📁 项目结构

```
love-order/
├── backend/                          # SpringBoot 后端
│   ├── pom.xml                       # Maven 依赖配置
│   └── src/main/
│       ├── resources/
│       │   ├── application.yml       # 应用配置
│       │   └── init.sql             # 数据库初始化脚本
│       └── java/com/loveorder/
│           ├── LoveOrderApplication.java    # 启动类
│           ├── config/               # 配置类
│           ├── common/               # 通用类（Result、异常处理）
│           ├── entity/               # JPA 实体（10个）
│           ├── repository/           # 数据访问层
│           ├── service/              # 业务逻辑层
│           ├── controller/           # 控制器层
│           ├── dto/                  # 数据传输对象
│           └── util/                 # 工具类
│
└── frontend/                         # UniApp 前端
    ├── package.json                  # 依赖配置
    ├── manifest.json                 # UniApp 配置
    ├── pages.json                    # 页面路由
    ├── uni.scss                      # 全局样式变量
    ├── App.vue                       # 应用入口
    ├── main.js                       # 入口文件
    ├── utils/                        # 工具模块
    │   ├── request.js                # 网络请求封装
    │   ├── auth.js                   # 登录认证
    │   └── date.js                   # 日期工具
    ├── store/                        # Pinia 状态管理
    │   ├── user.js                   # 用户状态
    │   └── order.js                  # 订单状态
    ├── components/                   # 通用组件
    │   ├── TabBar/                   # 底部导航
    │   ├── MenuItem/                 # 菜品卡片
    │   ├── HeartRating/              # 爱心评分
    │   ├── Countdown/                # 倒计时
    │   ├── CartFloat/                # 购物车浮窗
    │   ├── EmptyState/               # 空状态
    │   └── Loading/                  # 加载动画
    └── pages/                        # 页面
        ├── index/                    # 首页
        ├── menu/                     # 菜单
        ├── order/                    # 下单确认
        ├── orders/                   # 订单列表
        │   └── detail.vue            # 订单详情
        ├── profile/                  # 个人中心
        ├── anniversary/              # 纪念日管理
        ├── review/                   # 评价
        └── login/                    # 登录
```

## ✨ 核心功能

### 🏠 首页
- 情侣头像展示 + 在一起天数
- 纪念日倒计时（爱心动画）
- 每日推荐菜品（横向滚动）
- 心情点单入口（6种心情）
- 快捷下单

### 🍜 菜单
- 左侧分类导航（奶茶/甜品/小吃/主食/水果）
- 右侧菜品列表
- 搜索功能
- 心情标签筛选
- 加入购物车（爱心飞出动画）

### 🛒 下单
- 收单人信息确认
- 菜品清单
- 期望送达时间选择
- 甜蜜备注（快捷标签）
- 爱心积分抵扣

### 📋 订单管理
- 订单状态流转：待接单 → 制作中 → 送达中 → 已完成
- 订单详情查看
- 再来一单
- 评价功能（爱心评分）

### 💖 个人中心
- 爱心积分展示
- 每日签到（+1积分）
- 纪念日管理
- 我的评价
- 通知中心

### 🎉 特色功能
- **心情点单**：根据心情推荐菜品（开心/难过/生气/想念/惊喜/日常）
- **爱心积分**：下单+10分、评价+5分、签到+1分
- **纪念日提醒**：恋爱纪念日、生日、节日提醒
- **每日推荐**：每天随机推荐精选菜品
- **甜蜜备注**：预设浪漫备注标签

## 🗄️ 数据库设计

共 10 张表：

| 表名 | 说明 |
|------|------|
| `user` | 用户表（openid、角色、爱心积分） |
| `couple` | 情侣绑定表 |
| `menu_category` | 菜品分类 |
| `menu_item` | 菜品（含心情标签） |
| `orders` | 订单 |
| `order_item` | 订单明细 |
| `order_review` | 评价 |
| `anniversary` | 纪念日 |
| `daily_recommend` | 每日推荐 |
| `notification` | 通知 |

## 🔌 API 接口

| 模块 | 接口 | 说明 |
|------|------|------|
| 认证 | `POST /api/auth/login` | 微信登录 |
| 菜单 | `GET /api/menu/categories` | 获取分类 |
| 菜单 | `GET /api/menu/list` | 菜品列表 |
| 菜单 | `GET /api/menu/mood/{mood}` | 心情推荐 |
| 菜单 | `GET /api/menu/daily` | 每日推荐 |
| 订单 | `POST /api/order/create` | 创建订单 |
| 订单 | `GET /api/order/list` | 订单列表 |
| 订单 | `PUT /api/order/{id}/status` | 更新状态 |
| 用户 | `GET /api/user/info` | 个人信息 |
| 用户 | `POST /api/user/sign-in` | 每日签到 |
| 纪念日 | `GET /api/anniversary/list` | 纪念日列表 |
| 评价 | `POST /api/review/create` | 提交评价 |
| 通知 | `GET /api/notification/list` | 通知列表 |

## 📝 部署说明

### 后端部署
```bash
# 打包
cd backend
mvn clean package -DskipTests

# 运行
java -jar target/love-order-backend-1.0.0.jar
```

### 前端部署
```bash
# H5打包
cd frontend
npm run build:h5

# 微信小程序打包
npm run build:mp-weixin
```

## 📄 License

MIT License - 用爱发电 💕
