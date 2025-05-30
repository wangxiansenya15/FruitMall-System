package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.service.ProductService;
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
            return Result.error(Result.INTERNAL_ERROR, "获取商品详情失败，请稍后重试");
        }
    }
}
