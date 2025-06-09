-- 用户评论表
-- 用于存储用户对商品的评价信息，包括评分和评论内容

CREATE TABLE IF NOT EXISTS `reviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID，主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID，关联用户表',
    `product_id` BIGINT NOT NULL COMMENT '商品ID，关联商品表',
    `content` TEXT NOT NULL COMMENT '评论内容，用户对商品的文字评价',
    `rating` INT NOT NULL COMMENT '评分，范围0-5，0表示最差，5表示最好',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，记录评论发表的时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，记录评论最后修改的时间',
    
    -- 添加索引以提高查询性能
    INDEX `idx_product_id` (`product_id`) COMMENT '商品ID索引，用于快速查询商品的所有评论',
    INDEX `idx_user_id` (`user_id`) COMMENT '用户ID索引，用于快速查询用户的所有评论',
    INDEX `idx_rating` (`rating`) COMMENT '评分索引，用于按评分筛选和统计',
    INDEX `idx_create_time` (`create_time`) COMMENT '创建时间索引，用于按时间排序',
    
    -- 添加约束确保数据完整性
    CONSTRAINT `chk_rating_range` CHECK (`rating` >= 0 AND `rating` <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户评论表';

-- 插入测试数据
-- 为不同商品添加多样化的评论数据，展示不同评分和评论内容

INSERT INTO `reviews` (`user_id`, `product_id`, `content`, `rating`, `create_time`, `update_time`) VALUES
(1, 1, '苹果很新鲜，口感很好，包装也很精美，下次还会购买！', 5, '2024-01-15 10:30:00', '2024-01-15 10:30:00'),
(2, 1, '苹果质量不错，但是价格稍微有点贵，总体还是满意的。', 4, '2024-01-16 14:20:00', '2024-01-16 14:20:00'),
(3, 1, '收到的苹果有几个有点软了，可能是运输过程中的问题。', 3, '2024-01-17 09:15:00', '2024-01-17 09:15:00'),

(1, 2, '香蕉很甜，成熟度刚好，孩子们都很喜欢吃。', 5, '2024-01-18 16:45:00', '2024-01-18 16:45:00'),
(4, 2, '香蕉还可以，但是有些偏生，需要再放几天才能吃。', 3, '2024-01-19 11:30:00', '2024-01-19 11:30:00'),

(2, 3, '橙子很甜很多汁，维生素C含量高，全家人都爱吃。', 5, '2024-01-20 13:25:00', '2024-01-20 13:25:00'),
(5, 3, '橙子新鲜度一般，有几个已经开始变软了。', 2, '2024-01-21 08:40:00', '2024-01-21 08:40:00'),

(3, 4, '葡萄很甜，颗粒饱满，没有坏果，包装很用心。', 4, '2024-01-22 15:10:00', '2024-01-22 15:10:00'),
(6, 4, '葡萄味道不错，但是价格有点高，性价比一般。', 3, '2024-01-23 12:05:00', '2024-01-23 12:05:00'),

(4, 5, '草莓很新鲜，很甜，孩子特别喜欢，会继续购买。', 5, '2024-01-24 17:30:00', '2024-01-24 17:30:00'),
(1, 5, '草莓外观很好看，但是味道有点酸，可能还没完全成熟。', 3, '2024-01-25 10:15:00', '2024-01-25 10:15:00'),

(5, 6, '西瓜很甜很多汁，夏天吃特别解渴，推荐购买。', 5, '2024-01-26 14:50:00', '2024-01-26 14:50:00'),
(2, 6, '西瓜还行，但是不够甜，可能是品种的问题。', 3, '2024-01-27 09:20:00', '2024-01-27 09:20:00'),

(6, 7, '芒果很香很甜，果肉厚实，没有纤维感，非常满意。', 5, '2024-01-28 16:35:00', '2024-01-28 16:35:00'),
(3, 7, '芒果质量不错，但是有点贵，希望能有优惠活动。', 4, '2024-01-29 11:45:00', '2024-01-29 11:45:00'),

(1, 8, '猕猴桃很新鲜，酸甜适中，维生素含量高，很健康。', 4, '2024-01-30 13:15:00', '2024-01-30 13:15:00'),
(4, 8, '猕猴桃有点硬，需要放几天才能吃，但是味道还不错。', 3, '2024-01-31 08:30:00', '2024-01-31 08:30:00');

-- 查询统计信息的示例SQL
-- 这些查询可以用于验证数据和测试功能

-- 查询每个商品的平均评分和评论数量
SELECT 
    product_id,
    ROUND(AVG(rating), 1) as average_rating,
    COUNT(*) as review_count
FROM reviews 
GROUP BY product_id 
ORDER BY average_rating DESC;

-- 查询评分分布
SELECT 
    rating,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM reviews), 2) as percentage
FROM reviews 
GROUP BY rating 
ORDER BY rating DESC;

-- 查询最近的评论
SELECT 
    r.*,
    DATE_FORMAT(r.create_time, '%Y-%m-%d %H:%i') as formatted_time
FROM reviews r 
ORDER BY r.create_time DESC 
LIMIT 10;