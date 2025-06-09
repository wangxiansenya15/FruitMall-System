-- 订单表
CREATE TABLE IF NOT EXISTS `orders` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` int(11) NOT NULL COMMENT '用户ID',
    `order_number` varchar(50) NOT NULL COMMENT '订单号',
    `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
    `status` int(1) NOT NULL DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-已发货，3-已完成，4-已取消',
    `payment_method` int(1) NULL COMMENT '支付方式：1-支付宝，2-微信支付，3-银联支付，4-其他',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `pay_time` datetime NULL COMMENT '支付时间',
    `shipping_address` varchar(500) NULL COMMENT '收货地址',
    `consignee_name` varchar(50) NULL COMMENT '收货人姓名',
    `consignee_phone` varchar(20) NULL COMMENT '收货人电话',
    `remark` varchar(500) NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_number` (`order_number`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_payment_method` (`payment_method`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单详情表
CREATE TABLE IF NOT EXISTS `order_items` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
    `order_id` int(11) NOT NULL COMMENT '订单ID',
    `product_id` int(11) NOT NULL COMMENT '商品ID',
    `product_name` varchar(100) NOT NULL COMMENT '商品名称',
    `product_image` varchar(500) NULL COMMENT '商品图片',
    `price` decimal(10,2) NOT NULL COMMENT '商品单价',
    `quantity` int(11) NOT NULL COMMENT '购买数量',
    `subtotal` decimal(10,2) NOT NULL COMMENT '小计金额',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 插入测试数据
INSERT INTO `orders` (`user_id`, `order_number`, `total_amount`, `status`, `payment_method`, `shipping_address`, `consignee_name`, `consignee_phone`, `remark`) VALUES
(2, 'ORD202501010001', 89.50, 1, 1, '北京市朝阳区某某街道123号', '张三', '13800138000', '请尽快发货'),
(3, 'ORD202501010002', 156.80, 2, 2, '上海市浦东新区某某路456号', '李四', '13900139000', ''),
(2, 'ORD202501010003', 45.20, 0, NULL, '广州市天河区某某大道789号', '王五', '13700137000', '周末配送');

INSERT INTO `order_items` (`order_id`, `product_id`, `product_name`, `product_image`, `price`, `quantity`, `subtotal`) VALUES
(1, 1, '新鲜苹果', '/images/apple.jpg', 12.50, 3, 37.50),
(1, 2, '香甜橙子', '/images/orange.jpg', 26.00, 2, 52.00),
(2, 3, '进口香蕉', '/images/banana.jpg', 18.80, 4, 75.20),
(2, 1, '新鲜苹果', '/images/apple.jpg', 12.50, 2, 25.00),
(2, 4, '红心火龙果', '/images/dragon_fruit.jpg', 28.30, 2, 56.60),
(3, 2, '香甜橙子', '/images/orange.jpg', 26.00, 1, 26.00),
(3, 5, '新疆葡萄', '/images/grape.jpg', 19.20, 1, 19.20);