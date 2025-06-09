package com.wxs.service;

import com.wxs.dao.ProductMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;

    /**
     * 根据ID获取商品信息
     * @param id 商品ID
     * @return 商品对象
     */
    public Result<Product> getGoodNameById(Integer id) {
        log.info("商品ID: {}", id);
        if (id == null) {
            return Result.error(Result.BAD_REQUEST, "您还没登录，请先登录");
        }
        Product product = productMapper.selectGoodById(id);
        log.info("成功获取商品信息，商品信息: {}", product);
        return Result.success("商品详细信息已成功加载", product);
    }

    /**
     * 获取所有商品列表
     * 对应前端API: GET /products
     * @return 包含商品列表的Result对象
     */
    public Result<List<Product>> getAllGoods() {
        try {
            List<Product> products = productMapper.selectAllGoods();
            log.info("成功获取商品列表，共{}个商品", products.size());
            return Result.success("商品信息已成功加载", products);
        } catch (Exception e) {
            log.error("获取商品列表失败: {}", e.getMessage(), e);
            return Result.ServerError("获取商品列表失败，请稍后重试");
        }
    }

    /**
     * 获取所有商品分类
     * 对应前端API: GET /products/categories
     * @return 包含分类列表的Result对象
     */
    public Result<List<String>> getAllCategories() {
        try {
            List<String> categories = productMapper.selectAllCategories();
            log.info("成功获取商品分类，共{}个分类", categories.size());
            return Result.success("商品分类已成功加载", categories);
        } catch (Exception e) {
            log.error("获取商品分类失败: {}", e.getMessage(), e);
            return Result.ServerError("获取商品分类失败，请稍后重试");
        }
    }

    public String getGoodNameByName(String name) {
        return "Product " + name;
    }

    public String getGoodNameByPrice(Integer price) {
        return "Product " + price;
    }
}
