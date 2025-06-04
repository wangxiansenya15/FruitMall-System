/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80404 (8.4.4)
 Source Host           : localhost:3306
 Source Schema         : fruit_db

 Target Server Type    : MySQL
 Target Server Version : 80404 (8.4.4)
 File Encoding         : 65001

 Date: 02/06/2025 21:38:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart_items
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items` (
  `cart_item_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int DEFAULT '1',
  `unit_price` decimal(10,2) NOT NULL,
  `selected` tinyint(1) DEFAULT '1',
  `added_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_item_id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_items_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cart_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of cart_items
-- ----------------------------
BEGIN;
INSERT INTO `cart_items` (`cart_item_id`, `user_id`, `product_id`, `quantity`, `unit_price`, `selected`, `added_time`, `updated_time`) VALUES (19, 1, 1, 20, 0.00, 1, '2025-05-29 21:29:58', '2025-05-29 21:59:03');
INSERT INTO `cart_items` (`cart_item_id`, `user_id`, `product_id`, `quantity`, `unit_price`, `selected`, `added_time`, `updated_time`) VALUES (20, 1, 3, 20, 0.00, 1, '2025-05-29 21:31:23', '2025-05-29 21:31:27');
INSERT INTO `cart_items` (`cart_item_id`, `user_id`, `product_id`, `quantity`, `unit_price`, `selected`, `added_time`, `updated_time`) VALUES (22, 1, 5, 20, 0.00, 1, '2025-05-29 21:40:00', '2025-05-29 21:59:43');
COMMIT;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price` double NOT NULL,
  `count` int NOT NULL,
  `category` varchar(20) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of product
-- ----------------------------
BEGIN;
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (1, '苹果', 5.9, 100, '果类', '红富士苹果🍎，脆甜多汁', 'http://localhost:8080/product/apple.jpg', NULL, '2025-04-20 03:24:30', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (2, '橙子', 6.9, 150, NULL, '赣南脐橙，酸甜可口🍊', 'http://localhost:8080/product/orange.jpg', NULL, '2025-04-20 03:24:30', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (3, '葡萄', 8.99, 80, NULL, '无籽葡萄，新鲜上市🍇', 'http://localhost:8080/product/grape.jpg', NULL, '2025-04-20 03:24:30', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (4, '草莓', 69.9, 200, NULL, '丹东大草莓，全新上市🍓', 'http://localhost:8080/product/strawberry.jpg', NULL, '2025-04-21 20:52:23', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (5, '百香果', 5, 150, NULL, '酸甜可口', 'http://localhost:8080/product/passionfruit.jpg', NULL, '2025-04-21 20:54:19', '2025-05-29 12:17:56');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (6, '蓝莓', 30, 550, '浆果类', '清甜🫐', 'http://localhost:8080/product/blueberry.jpg', NULL, '2025-04-21 20:56:54', '2025-05-29 12:17:56');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (7, '西瓜', 3.5, 140, '瓜类', '清甜多汁🍉', 'http://localhost:8080/product/watermelon.jpg', NULL, '2025-04-21 20:57:36', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (8, '凤梨', 4.5, 1000, NULL, '香甜🍍', 'http://localhost:8080/product/pineapple.jpg', NULL, '2025-04-21 20:58:26', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (9, '车厘子', 25, 144, '浆果类', '智利进口车厘子🍒', 'http://localhost:8080/product/cherry.jpg', NULL, '2025-04-21 20:58:49', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (10, '榴莲', 35, 100, '热带水果', '过期榴莲奇臭无比', 'http://localhost:8080/product/durian.jpg', NULL, '2025-04-21 21:45:03', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (11, '猕猴桃', 5, 50, '热带水果', '酸涩猕猴桃酸到变形🥝', 'http://localhost:8080/product/kiwi.jpg', NULL, '2025-04-21 21:45:03', '2025-05-29 12:17:56');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (12, '香蕉', 7, 1200, '热带水果', '过熟的香蕉熟透软烂', 'http://localhost:8080/product/banana.jpg', NULL, '2025-04-21 21:45:03', '2025-05-29 12:17:56');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (23, '椰青', 8, 15, '热带水果', '海南高质量椰子🥥', 'http://localhost:8080/product/coconut.jpg', NULL, '2025-05-28 23:23:31', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (24, '哈密瓜', 11.5, 110, '瓜类', '新疆哈密瓜🍈，巨甜', 'http://localhost:8080/product/hami-melon.jpg', 5, '2025-05-28 23:23:31', '2025-05-29 21:24:48');
INSERT INTO `product` (`product_id`, `name`, `price`, `count`, `category`, `remark`, `image_url`, `rating`, `create_time`, `update_time`) VALUES (25, '山竹', 21, 260, '热带水果', '泰国进口山竹', 'http://localhost:8080/product/mangosteen.jpg', NULL, '2025-05-29 10:29:04', '2025-05-29 21:24:48');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(75) NOT NULL,
  `password` varchar(200) NOT NULL,
  `email` varchar(200) DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `role` enum('ADMIN','SUPER_ADMIN','USER') DEFAULT 'USER',
  `nickname` varchar(50) DEFAULT NULL,
  `register_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updateInfo_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `account_non_expired` tinyint(1) NOT NULL DEFAULT '1',
  `account_non_locked` tinyint(1) NOT NULL DEFAULT '1',
  `credentials_non_expired` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `email_2` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (1, 'admin', '$2a$12$/TLoYPZCQ75oVT8eJ7q9QO5WzVxB0mSNXT9RQZFGXBJhnfS5uhszi', '198@wxs.com', '19820041121', 'SUPER_ADMIN', '2', '2025-04-20 11:38:50', '2025-05-28 22:52:21', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (2, 'Arthur', '$2a$12$afef82bNNYvVgKCPrl8x8uT0ZrUSDM6a7En5Rdf81Gs7CqoTWMQA6', 'arthur@qq.com', NULL, 'SUPER_ADMIN', NULL, '2025-04-20 11:40:02', '2025-05-20 20:32:37', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (3, 'wxs', '$2a$12$lxBUm0tP8bE7zM7DxVUi/ulmdVIYwDTPX27xiPb..dcafi9e7zFd6', 'wxs@www.wxs.com', NULL, 'SUPER_ADMIN', NULL, '2025-04-20 11:39:13', '2025-05-20 20:32:37', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (4, 'xiaojie', '$2a$12$8/PUYWO3nhGbBu794O4SV.zMWJzNaHgo5AjFtJL4VYwmmxANhe0Sm', 'xaiojie520@www.wxs.com', NULL, 'USER', NULL, '2025-04-20 11:39:52', '2025-05-18 03:38:12', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (5, 'xiaofei', '$2a$12$s27Q894yJ/IZ6dwJthT9leCD1kGKvPhRqN5ErMMfHpZPPaMIq0hRa', 'lyf@wxs.com', NULL, 'USER', NULL, '2025-04-27 10:39:59', '2025-05-18 03:38:12', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (6, 'JayChou', '$2a$12$Idi0zEV.zijxKTjSP1pQiuLMVnuqpwaTpJ96cF/6OUWSAae2I.FLe', 'jaychou@gmail.com', NULL, 'USER', NULL, '2025-04-27 10:40:15', '2025-05-18 03:38:12', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (7, 'xiaohui', '$2a$12$WmlHQ63j2UbG369glecv6ufN8nfYtoEqB8DSORlz5.q2YUm46/ydy', 'huihui@wxs.com', NULL, 'ADMIN', NULL, '2025-04-28 20:20:05', '2025-05-25 22:46:32', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (8, 'wjy3', '', 'wjy1234@qq.com', NULL, 'USER', NULL, '2025-04-28 20:20:40', '2025-05-29 22:56:29', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (9, 'wxs666', '', '198@163.com', NULL, 'USER', NULL, '2025-05-17 22:17:54', '2025-05-25 21:48:42', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (11, 'test', '$2a$12$RNR2XHHFo/pnLYq27Blc0unpKjga8lZfvLCzSXVYfVoPwJwnb1CWu', 'test@example.com', NULL, 'USER', NULL, '2025-05-13 16:03:09', '2025-05-18 03:40:10', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (15, 'baby', '$2a$12$OHU3kMngW1Hj8YQ2RYw7duDr12Z04HinJxt1kCnZs5OnwJGlaUHri', 'baby@example.com', NULL, 'USER', NULL, '2025-05-13 16:04:18', '2025-05-18 03:40:10', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (17, 'sweetbaby', '$2a$12$4dFo.eBS8JCB0G6ub2ouHu.k.lGrp/j53E665Z7M9SKvi9VkMV3wm', 'sbaby@163.com', NULL, 'USER', NULL, '2025-05-17 17:01:25', '2025-05-18 03:40:10', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (22, 'mike123', '$2a$12$mbqAnTi0iTi3ZDXfNxhhbOB.lNZi9HwrrL8YbtOEMqx/.Csw2pfYG', 'mike@123.com', NULL, 'USER', NULL, '2025-05-26 20:55:48', '2025-05-27 15:03:39', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (23, 'Alice123', '$2a$12$8f3PlhCMekX67/xk094RIeF0o277nCRbfqafutbRVO3NiPYmC.QO2', 'alice@163.com', NULL, 'USER', NULL, '2025-05-27 15:03:09', '2025-05-27 15:03:09', 1, 1, 1, 1);
INSERT INTO `user` (`id`, `username`, `password`, `email`, `phone`, `role`, `nickname`, `register_time`, `updateInfo_time`, `enabled`, `account_non_expired`, `account_non_locked`, `credentials_non_expired`) VALUES (26, 'ysy', '$2a$12$HlcRW3JSPgqIAitnTZskTuz1DnVkWo4dFvH0J1HAShwjVbWiBBB86', 'ysylove@wxs.com', NULL, 'USER', NULL, '2025-05-28 20:32:44', '2025-05-28 20:32:44', 1, 1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for user_details
-- ----------------------------
DROP TABLE IF EXISTS `user_details`;
CREATE TABLE `user_details` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `description` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户描述',
  `avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'https://www.wxs.org/img/avatar.png' COMMENT '头像地址',
  `age` int DEFAULT NULL,
  `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender` enum('男','女','NULL') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `user_details_chk_1` CHECK (((`age` >= 0) and (`age` <= 150)))
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户详细信息表';

-- ----------------------------
-- Records of user_details
-- ----------------------------
BEGIN;
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (1, '我是水果商城系统的超级管理员', 'http://localhost:8080/avatar/16c863604a4bd6fed468918a65a6fec.jpg', 20, '广东省肇庆市', '男');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (2, 'Arthur是管理员的英文名', 'https://www.wxs.org/img/avatar.png', 21, '硅谷，美国🇺🇸', '男');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (3, 'wxs是管理员名字的缩写', 'https://www.wxs.org/img/avatar.png', 18, '广东省广州市，中国🇨🇳', '男');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (4, '小洁是一位可爱的女生', 'http://localhost:8080/avatar/xiaojie.png', 20, '广东省清远市', '女');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (5, '小菲是我的女神', 'https://www.wxs.org/img/lyf.png', 40, '多伦多，加拿大🇨🇦', '女');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (6, '我最爱听周董的歌', 'https://www.wxs.org/img/jay.png', 50, '台湾省台北市，中国🇨🇳', '男');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (7, '惠崽是我的宝宝', 'https://www.wxs.org/img/szh.png', 19, '江苏省徐州市', '女');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (8, 'wjy是个呆b妹妹', 'https://www.wxs.org/img/avatar.png', 11, '广东省惠州市', '女');
INSERT INTO `user_details` (`id`, `description`, `avatar`, `age`, `address`, `gender`) VALUES (26, NULL, 'https://www.wxs.org/img/avatar.png', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_favorites
-- ----------------------------
DROP TABLE IF EXISTS `user_favorites`;
CREATE TABLE `user_favorites` (
  `favorite_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`favorite_id`),
  UNIQUE KEY `unique_user_product` (`user_id`,`product_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_created_time` (`created_time`),
  CONSTRAINT `user_favorites_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_favorites_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户收藏表';

-- ----------------------------
-- Records of user_favorites
-- ----------------------------
BEGIN;
INSERT INTO `user_favorites` (`favorite_id`, `user_id`, `product_id`, `created_time`) VALUES (2, 1, 2, '2025-05-30 07:48:04');
INSERT INTO `user_favorites` (`favorite_id`, `user_id`, `product_id`, `created_time`) VALUES (3, 1, 5, '2025-05-30 07:48:06');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
