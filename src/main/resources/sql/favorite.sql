-- 用户收藏表
CREATE TABLE user_favorites (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,  -- 收藏记录唯一ID
    user_id     INT NOT NULL,                    -- 关联用户ID
    product_id  INT NOT NULL,                    -- 关联商品ID
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP, -- 收藏时间
    
    -- 外键约束
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    
    -- 唯一约束：同一用户不能重复收藏同一商品
    UNIQUE KEY unique_user_product (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 创建索引提高查询性能
CREATE INDEX idx_user_id ON user_favorites(user_id);
CREATE INDEX idx_product_id ON user_favorites(product_id);
CREATE INDEX idx_created_time ON user_favorites(created_time);