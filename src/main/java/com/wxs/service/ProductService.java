package com.wxs.service;

import com.wxs.dao.ProductMapper;
import com.wxs.pojo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;

    public Product getGoodNameById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID不能为空");
        }
        return productMapper.selectGoodById(id);
    }

    public String getGoodNameByName(String name) {
        return "Product " + name;
    }

    public String getGoodNameByPrice(Integer price) {
        return "Product " + price;
    }
}
