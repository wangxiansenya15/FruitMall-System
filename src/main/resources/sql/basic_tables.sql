







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




-- 联系我们留言表
CREATE TABLE contact_messages (
                                  msg_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                                  username VARCHAR(50) NOT NULL COMMENT '用户名',
                                  email VARCHAR(100) NOT NULL COMMENT '邮箱地址',
                                  problem VARCHAR(200) NOT NULL COMMENT '问题反馈',
                                  message TEXT NOT NULL COMMENT '留言内容',
                                  submit_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
                                  status TINYINT DEFAULT 0 COMMENT '处理状态：0-未处理，1-处理中，2-已处理',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系我们留言表';


-- 店铺管理表
CREATE TABLE shop (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      store_name VARCHAR(100) NOT NULL,
                      address TEXT,
                      phone VARCHAR(20),
                      status ENUM('营业中', '休息中', '节假日休息', '系统维护中') DEFAULT '营业中', -- 状态字段
                      open_time TIME,
                      close_time TIME,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);