SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `openid` VARCHAR(64) NOT NULL,
    `nickname` VARCHAR(64) DEFAULT NULL,
    `avatar` VARCHAR(512) DEFAULT NULL,
    `role` VARCHAR(16) NOT NULL DEFAULT 'girlfriend',
    `love_points` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `couple`;
CREATE TABLE `couple` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `boy_id` BIGINT NOT NULL,
    `girl_id` BIGINT NOT NULL,
    `couple_name` VARCHAR(64) DEFAULT NULL,
    `anniversary` DATE DEFAULT NULL,
    `bind_date` DATETIME DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_boy_girl` (`boy_id`, `girl_id`),
    KEY `idx_girl_id` (`girl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `menu_category`;
CREATE TABLE `menu_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    `icon` VARCHAR(256) DEFAULT NULL,
    `sort_order` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `menu_item`;
CREATE TABLE `menu_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `category_id` BIGINT NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    `description` VARCHAR(512) DEFAULT NULL,
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `image` VARCHAR(512) DEFAULT NULL,
    `mood_tag` VARCHAR(16) DEFAULT NULL,
    `is_daily_recommend` TINYINT(1) NOT NULL DEFAULT 0,
    `sort_order` INT NOT NULL DEFAULT 0,
    `status` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_mood_tag` (`mood_tag`),
    KEY `idx_status_sort` (`status`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(32) NOT NULL,
    `boy_id` BIGINT NOT NULL,
    `girl_id` BIGINT NOT NULL,
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `status` VARCHAR(16) NOT NULL DEFAULT 'pending',
    `remark` VARCHAR(512) DEFAULT NULL,
    `delivery_time` DATETIME DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_boy_id` (`boy_id`),
    KEY `idx_girl_id` (`girl_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `menu_item_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_menu_item_id` (`menu_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `order_review`;
CREATE TABLE `order_review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `reviewer_id` BIGINT NOT NULL,
    `rating` TINYINT NOT NULL,
    `content` VARCHAR(512) DEFAULT NULL,
    `heart_count` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_reviewer_id` (`reviewer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `anniversary`;
CREATE TABLE `anniversary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `couple_id` BIGINT NOT NULL,
    `title` VARCHAR(128) NOT NULL,
    `date` DATE NOT NULL,
    `type` VARCHAR(16) NOT NULL DEFAULT 'custom',
    `remind_days` INT NOT NULL DEFAULT 3,
    `is_recurring` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_couple_id` (`couple_id`),
    KEY `idx_date` (`date`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `daily_recommend`;
CREATE TABLE `daily_recommend` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `menu_item_id` BIGINT NOT NULL,
    `recommend_date` DATE NOT NULL,
    `reason` VARCHAR(256) DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_item_date` (`menu_item_id`, `recommend_date`),
    KEY `idx_recommend_date` (`recommend_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `type` VARCHAR(32) NOT NULL DEFAULT 'system',
    `title` VARCHAR(128) NOT NULL,
    `content` VARCHAR(512) DEFAULT NULL,
    `is_read` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_user_read` (`user_id`, `is_read`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- 初始数据
INSERT INTO `menu_category` (`name`, `icon`, `sort_order`) VALUES
('奶茶', '/icons/milk-tea.png', 1),
('甜品', '/icons/dessert.png', 2),
('小吃', '/icons/snack.png', 3),
('主食', '/icons/main-food.png', 4),
('水果', '/icons/fruit.png', 5);

INSERT INTO `menu_item` (`category_id`, `name`, `description`, `price`, `mood_tag`, `is_daily_recommend`, `sort_order`, `status`) VALUES
(1, '想你奶茶', '每一口都是对你的想念', 15.00, '想念', 1, 1, 1),
(1, '甜蜜双拼', '双倍甜蜜双倍快乐', 18.00, '开心', 1, 2, 1),
(1, '恋爱酸酸甜甜', '酸酸甜甜就是我', 16.00, '开心', 0, 3, 1),
(1, '暖心暖胃拿铁', '天冷了温暖你', 20.00, '日常', 0, 4, 1),
(1, '哄你开心奶茶', '别生气啦', 17.00, '生气', 0, 5, 1),
(1, '惊喜波波奶', '生活需要小惊喜', 19.00, '惊喜', 1, 6, 1),
(1, '初恋草莓奶昔', '像初恋一样美好', 22.00, '开心', 0, 7, 1),
(1, '晚安热可可', '梦里也要甜甜的', 18.00, '日常', 0, 8, 1),
(2, '爱你蛋糕', '一口一口吃掉甜蜜', 28.00, '开心', 1, 1, 1),
(2, '心动提拉米苏', '心层层叠叠', 32.00, '惊喜', 0, 2, 1),
(2, '抱抱布丁', '像拥抱一样温暖', 15.00, '想念', 0, 3, 1),
(2, '彩虹千层', '五彩缤纷的爱情', 35.00, '开心', 1, 4, 1),
(2, '治愈系马卡龙', '世界都变美好了', 25.00, '难过', 0, 5, 1),
(2, '蜜蜜芝士', '爱你就是甜蜜', 26.00, '日常', 0, 6, 1),
(3, '恋爱爆米花', '看电影必备', 12.00, '开心', 0, 1, 1),
(3, '撒娇薯条', '给我吃一根嘛', 10.00, '开心', 0, 2, 1),
(3, '暖心炸鸡', '心情不好的时候', 25.00, '难过', 0, 3, 1),
(3, '甜蜜鸡翅', '甜到心里', 22.00, '开心', 0, 4, 1),
(3, '惊喜盲盒小吃', '每天都是惊喜', 18.00, '惊喜', 1, 5, 1),
(4, '爱心便当', '满满的爱意', 30.00, '想念', 1, 1, 1),
(4, '幸福面条', '长长久久的爱情', 20.00, '日常', 0, 2, 1),
(4, '约会牛排', '烛光晚餐标配', 68.00, '惊喜', 0, 3, 1),
(4, '暖心粥', '生病了陪你', 18.00, '难过', 0, 4, 1),
(4, '元气满满饭团', '元气满满', 15.00, '开心', 0, 5, 1),
(5, '甜蜜果盘', '甜蜜又健康', 25.00, '开心', 0, 1, 1),
(5, '想你草莓', '时刻想着你', 20.00, '想念', 1, 2, 1),
(5, '元气水果捞', '满满的元气', 22.00, '日常', 0, 3, 1),
(5, '惊喜水果盲盒', '打开看看吧', 28.00, '惊喜', 0, 4, 1),
(5, '哄你西瓜汁', '消消气凉快凉快', 15.00, '生气', 0, 5, 1);

INSERT INTO `user` (`openid`, `nickname`, `avatar`, `role`, `love_points`) VALUES
('boy_openid_001', '你的专属外卖员', '/avatars/boy.png', 'boyfriend', 520),
('girl_openid_001', '全世界最可爱', '/avatars/girl.png', 'girlfriend', 1314);

INSERT INTO `couple` (`boy_id`, `girl_id`, `couple_name`, `anniversary`) VALUES
(1, 2, '小甜心和小可爱', '2025-02-14');

INSERT INTO `anniversary` (`couple_id`, `title`, `date`, `type`, `remind_days`, `is_recurring`) VALUES
(1, '恋爱纪念日', '2025-02-14', '恋爱', 7, 1),
(1, '她的生日', '2000-05-20', '生日', 14, 1),
(1, '他的生日', '1999-08-15', '生日', 14, 1),
(1, '七夕节', '2025-08-29', '节日', 7, 1),
(1, '情人节', '2026-02-14', '节日', 7, 1);

INSERT INTO `orders` (`order_no`, `boy_id`, `girl_id`, `total_amount`, `status`, `remark`, `delivery_time`) VALUES
('LOVE20260516001', 1, 2, 33.00, 'completed', '今天心情不好，给你点了最爱喝的奶茶', '2026-05-16 14:30:00'),
('LOVE20260516002', 1, 2, 50.00, 'cooking', '下午茶时间到！', '2026-05-16 15:00:00');

INSERT INTO `order_item` (`order_id`, `menu_item_id`, `quantity`, `price`) VALUES
(1, 1, 1, 15.00),
(1, 10, 1, 18.00),
(2, 18, 1, 18.00),
(2, 6, 1, 19.00),
(2, 8, 1, 13.00);

INSERT INTO `order_review` (`order_id`, `reviewer_id`, `rating`, `content`, `heart_count`) VALUES
(1, 2, 5, '奶茶很好喝！心情瞬间变好了，谢谢你亲爱的~', 10);

INSERT INTO `daily_recommend` (`menu_item_id`, `recommend_date`, `reason`) VALUES
(1, '2026-05-16', '今天天气很好，适合喝一杯想念的味道'),
(6, '2026-05-16', '周五啦！来一杯惊喜波波奶犒劳一下'),
(9, '2026-05-16', '爱你蛋糕，周末就是表达爱的最佳时机'),
(23, '2026-05-16', '爱心便当，周末给她做一顿爱心满满的午餐吧');

INSERT INTO `notification` (`user_id`, `type`, `title`, `content`, `is_read`) VALUES
(2, 'order', '收到一份爱心订单', '你的专属外卖员给你点了一份奶茶', 0),
(2, 'anniversary', '纪念日提醒', '距离恋爱纪念日还有30天哦', 0),
(1, 'review', '收到新的评价', '全世界最可爱给你的订单打了5星好评', 1),
(2, 'system', '欢迎来到爱心点单', '欢迎使用爱心点单小程序', 0);
操作步骤：

在 Navicat 中新建查询
全选复制上面的 SQL 内容粘贴进去
点击运行
如果还有问题请告诉我！


image.png
PNG
10:58

参考内容


已生成兼容版 SQL 文件。或者直接复制下面的内容执行：


SQL

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `openid` VARCHAR(64) NOT NULL,
    `nickname` VARCHAR(64) DEFAULT NULL,
    `avatar` VARCHAR(512) DEFAULT NULL,
    `role` VARCHAR(16) NOT NULL DEFAULT 'girlfriend',
    `love_points` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `couple`;
CREATE TABLE `couple` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `boy_id` BIGINT NOT NULL,
    `girl_id` BIGINT NOT NULL,
    `couple_name` VARCHAR(64) DEFAULT NULL,
    `anniversary` DATE DEFAULT NULL,
    `bind_date` DATETIME DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_boy_girl` (`boy_id`, `girl_id`),
    KEY `idx_girl_id` (`girl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `menu_category`;
CREATE TABLE `menu_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    `icon` VARCHAR(256) DEFAULT NULL,
    `sort_order` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `menu_item`;
CREATE TABLE `menu_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `category_id` BIGINT NOT NULL,
    `name` VARCHAR(64) NOT NULL,
    `description` VARCHAR(512) DEFAULT NULL,
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `image` VARCHAR(512) DEFAULT NULL,
    `mood_tag` VARCHAR(16) DEFAULT NULL,
    `is_daily_recommend` TINYINT(1) NOT NULL DEFAULT 0,
    `sort_order` INT NOT NULL DEFAULT 0,
    `status` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_mood_tag` (`mood_tag`),
    KEY `idx_status_sort` (`status`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(32) NOT NULL,
    `boy_id` BIGINT NOT NULL,
    `girl_id` BIGINT NOT NULL,
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `status` VARCHAR(16) NOT NULL DEFAULT 'pending',
    `remark` VARCHAR(512) DEFAULT NULL,
    `delivery_time` DATETIME DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_boy_id` (`boy_id`),
    KEY `idx_girl_id` (`girl_id`),
    KEY `idx_status` (`status`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `menu_item_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_menu_item_id` (`menu_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `order_review`;
CREATE TABLE `order_review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `reviewer_id` BIGINT NOT NULL,
    `rating` TINYINT NOT NULL,
    `content` VARCHAR(512) DEFAULT NULL,
    `heart_count` INT NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_reviewer_id` (`reviewer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `anniversary`;
CREATE TABLE `anniversary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `couple_id` BIGINT NOT NULL,
    `title` VARCHAR(128) NOT NULL,
    `date` DATE NOT NULL,
    `type` VARCHAR(16) NOT NULL DEFAULT 'custom',
    `remind_days` INT NOT NULL DEFAULT 3,
    `is_recurring` TINYINT(1) NOT NULL DEFAULT 1,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_couple_id` (`couple_id`),
    KEY `idx_date` (`date`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `daily_recommend`;
CREATE TABLE `daily_recommend` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `menu_item_id` BIGINT NOT NULL,
    `recommend_date` DATE NOT NULL,
    `reason` VARCHAR(256) DEFAULT NULL,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_item_date` (`menu_item_id`, `recommend_date`),
    KEY `idx_recommend_date` (`recommend_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `type` VARCHAR(32) NOT NULL DEFAULT 'system',
    `title` VARCHAR(128) NOT NULL,
    `content` VARCHAR(512) DEFAULT NULL,
    `is_read` TINYINT(1) NOT NULL DEFAULT 0,
    `created_at` DATETIME DEFAULT NULL,
    `updated_at` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_user_read` (`user_id`, `is_read`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;