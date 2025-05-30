







-- 员工表
CREATE TABLE `staff` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `name` varchar(50) NOT NULL,
                         `role` enum('admin','service','warehouse','delivery') NOT NULL,
                         `phone` varchar(20) NOT NULL,
                         `email` varchar(100) NOT NULL,
                         `password` varchar(100) NOT NULL,
                         `status` enum('active','inactive') DEFAULT 'active',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `phone` (`phone`),
                         UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 购物车表
CREATE TABLE cart_items (
                            cart_item_id INT PRIMARY KEY AUTO_INCREMENT, -- 购物车项唯一ID
                            user_id      INT NOT NULL,                   -- 关联用户ID
                            product_id   INT NOT NULL,                   -- 关联商品ID
                            quantity     INT DEFAULT 1,                  -- 商品数量（默认1）
                            unit_price   DECIMAL(10,2) NOT NULL,         -- 加入购物车时的单价（防价格变动）
                            selected     BOOLEAN DEFAULT TRUE,           -- 是否选中（用于结算时部分删除）
                            added_time     DATETIME DEFAULT CURRENT_TIMESTAMP, -- 加入时间
                            updated_time   DATETIME ON UPDATE CURRENT_TIMESTAMP, -- 最后修改时间
    -- 外键约束
                            FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);