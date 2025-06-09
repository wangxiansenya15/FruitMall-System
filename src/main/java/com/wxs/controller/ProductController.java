package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Review;
import com.wxs.service.ProductService;
import com.wxs.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 * 提供商品相关的REST API接口
 */
@RestController
@RequestMapping("/products")
@Slf4j
@CrossOrigin(origins = "*") // 允许跨域请求，便于前端调用
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ReviewService reviewService;

    /**
     * 获取所有商品列表
     * 对应前端调用: GET /products
     * @return 商品列表数据
     */
    @GetMapping
    public Result<?> getAllProducts() {
        log.info("接收到获取商品列表的请求");
        return productService.getAllGoods();
    }

    /**
     * 获取所有商品分类
     * 对应前端调用: GET /products/categories
     * @return 商品分类列表
     */
    @GetMapping("/categories")
    public Result<?> getAllCategories() {
        log.info("接收到获取商品分类的请求");
        return productService.getAllCategories();
    }

    /**
     * 根据ID获取单个商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    public Result<?> getProductById(@PathVariable Integer id) {
        log.info("接收到获取商品详情的请求，商品ID: {}", id);
        try {
            if (id == null) {
                return Result.badRequest("商品ID不能为空");
            }
            log.info("成功获取商品详情，商品信息: {}",productService.getGoodNameById(id));
            return productService.getGoodNameById(id);
        } catch (Exception e) {
            log.error("获取商品详情失败，商品ID: {}, 错误: {}", id, e.getMessage(), e);
            return Result.ServerError("获取商品详情失败，请稍后重试");
        }
    }
    
    // ==================== 评论相关接口 ====================

    /**
     * 添加商品评论 - 兼容前端路径
     * @param productId 商品ID
     * @param review 评论信息
     * @return 操作结果
     */
    @PostMapping("/{productId}/reviews")
    public Result<?> addProductReview(@PathVariable Long productId, @RequestBody Review review) {
        log.info("接收到添加评论的请求，商品ID: {}, 评分: {}", productId, review.getRating());
        // 设置商品ID
        review.setProductId(productId);
        return reviewService.addReview(review);
    }
    
    /**
     * 获取商品的所有评论
     * 商品详情页展示用户评论
     * @param productId 商品ID
     * @return 评论列表
     */
    @GetMapping("/{productId}/reviews")
    public Result<?> getProductReviews(@PathVariable Long productId) {
        log.info("接收到获取商品评论的请求，商品ID: {}", productId);
        return reviewService.getReviewsByProductId(productId);
    }
    
    /**
     * 获取用户的所有评论
     * 用户个人中心查看评论历史
     * @param userId 用户ID
     * @return 评论列表
     */
    @GetMapping("/reviews/user/{userId}")
    public Result<?> getUserReviews(@PathVariable Long userId) {
        log.info("接收到获取用户评论的请求，用户ID: {}", userId);
        return reviewService.getReviewsByUserId(userId);
    }
    
    /**
     * 更新评论
     * 用户修改自己的评论内容和评分
     * @param review 包含更新信息的评论对象
     * @return 操作结果
     */
    @PutMapping("/reviews")
    public Result<?> updateReview(@RequestBody Review review) {
        log.info("接收到更新评论的请求，评论ID: {}, 新评分: {}", review.getId(), review.getRating());
        return reviewService.updateReview(review);
    }
    
    /**
     * 删除评论
     * 用户删除自己的评论
     * @param reviewId 评论ID
     * @param userId 用户ID，用于权限验证
     * @return 操作结果
     */
    @DeleteMapping("/reviews/{reviewId}")
    public Result<?> deleteReview(@PathVariable Long reviewId, @RequestParam Long userId) {
        log.info("接收到删除评论的请求，评论ID: {}, 用户ID: {}", reviewId, userId);
        return reviewService.deleteReview(reviewId, userId);
    }
    
    /**
     * 获取商品评分统计
     * 获取商品的平均评分和评论总数
     * @param productId 商品ID
     * @return 评分统计信息
     */
    @GetMapping("/{productId}/rating-stats")
    public Result<?> getProductRatingStats(@PathVariable Long productId) {
        log.info("接收到获取商品评分统计的请求，商品ID: {}", productId);
        return reviewService.getProductRatingStats(productId);
    }
}
