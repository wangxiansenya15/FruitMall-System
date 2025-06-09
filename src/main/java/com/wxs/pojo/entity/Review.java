package com.wxs.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户评论实体类
 * 用于存储用户对商品的评价信息，包括评分和评论内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    /**
     * 评论ID，主键
     */
    private Long id;
    
    /**
     * 用户ID，关联用户表
     * 标识哪个用户发表了这条评论
     */
    private Long userId;
    
    /**
     * 商品ID，关联商品表
     * 标识评论针对的是哪个商品
     */
    private Long productId;
    
    /**
     * 评论内容
     * 用户对商品的文字评价
     */
    private String content;
    
    /**
     * 评分，范围0-5
     * 用户对商品的数字评价，0表示最差，5表示最好
     */
    private Integer rating;
    
    /**
     * 创建时间
     * 记录评论发表的时间，用于排序和显示
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     * 记录评论最后修改的时间
     */
    private LocalDateTime updateTime;
}