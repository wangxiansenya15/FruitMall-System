package com.wxs.service;

import com.wxs.dao.FavoriteMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.Favorite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收藏服务层
 * 处理收藏相关的业务逻辑
 */
@Service
@Slf4j
public class FavoriteService {
    
    @Autowired
    private FavoriteMapper favoriteMapper;

    /**
     * 添加商品到收藏夹
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 操作结果
     */
    public Result<Void> addToFavorites(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            return Result.badRequest("用户ID和商品ID不能为空");
        }

        try {
            // 检查是否已经收藏
            int existCount = favoriteMapper.checkFavoriteExists(userId, productId);
            if (existCount > 0) {
                return Result.badRequest("该商品已在收藏夹中");
            }

            // 创建收藏对象
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setProductId(productId);

            // 执行添加操作
            int result = favoriteMapper.insertFavorite(favorite);
            if (result > 0) {
                log.info("用户{}成功收藏商品{}", userId, productId);
                return Result.success("收藏成功");
            } else {
                log.warn("用户{}收藏商品{}失败", userId, productId);
                return Result.serverError("收藏失败，请稍后重试");
            }
        } catch (Exception e) {
            log.error("添加收藏时发生异常，用户ID: {}, 商品ID: {}, 错误: {}", userId, productId, e.getMessage(), e);
            return Result.serverError("收藏失败，请稍后重试");
        }
    }

    /**
     * 从收藏夹移除商品
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 操作结果
     */
    public Result<Void> removeFromFavorites(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            return Result.badRequest("用户ID和商品ID不能为空");
        }

        try {
            int result = favoriteMapper.deleteFavorite(userId, productId);
            if (result > 0) {
                log.info("用户{}成功取消收藏商品{}", userId, productId);
                return Result.success("取消收藏成功");
            } else {
                log.warn("用户{}取消收藏商品{}失败，可能该商品未被收藏", userId, productId);
                return Result.badRequest("该商品未在收藏夹中");
            }
        } catch (Exception e) {
            log.error("移除收藏时发生异常，用户ID: {}, 商品ID: {}, 错误: {}", userId, productId, e.getMessage(), e);
            return Result.serverError("取消收藏失败，请稍后重试");
        }
    }

    /**
     * 获取用户收藏列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    public Result<List<Favorite>> getFavoritesList(Integer userId) {
        if (userId == null) {
            return Result.error(Result.BAD_REQUEST,"用户ID不能为空");
        }

        try {
            List<Favorite> favorites = favoriteMapper.selectFavoritesByUserId(userId);
            log.info("用户{}的收藏列表查询成功，共{}个收藏", userId, favorites.size());
            return Result.success("获取收藏列表成功", favorites);
        } catch (Exception e) {
            log.error("获取收藏列表时发生异常，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.error(Result.INTERNAL_ERROR,"获取收藏列表失败，请稍后重试");
        }
    }

    /**
     * 检查商品是否已被用户收藏
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 是否已收藏
     */
    public Result<Boolean> isFavorited(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            return Result.error(Result.BAD_REQUEST,"用户ID和商品ID不能为空");
        }

        try {
            int count = favoriteMapper.checkFavoriteExists(userId, productId);
            boolean isFavorited = count > 0;
            return Result.success("查询成功", isFavorited);
        } catch (Exception e) {
            log.error("检查收藏状态时发生异常，用户ID: {}, 商品ID: {}, 错误: {}", userId, productId, e.getMessage(), e);
            return Result.error(Result.INTERNAL_ERROR,"查询失败，请稍后重试");
        }
    }

    /**
     * 清空用户所有收藏
     * @param userId 用户ID
     * @return 操作结果
     */
    public Result<Void> clearAllFavorites(Integer userId) {
        if (userId == null) {
            return Result.badRequest("用户ID不能为空");
        }

        try {
            int result = favoriteMapper.deleteAllFavoritesByUserId(userId);
            log.info("用户{}清空收藏夹成功，删除了{}条记录", userId, result);
            return Result.success("清空收藏夹成功");
        } catch (Exception e) {
            log.error("清空收藏夹时发生异常，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.serverError("清空收藏夹失败，请稍后重试");
        }
    }

    /**
     * 获取用户收藏总数
     * @param userId 用户ID
     * @return 收藏总数
     */
    public Result<Integer> getFavoriteCount(Integer userId) {
        if (userId == null) {
            return Result.error(Result.BAD_REQUEST,"用户ID不能为空");
        }

        try {
            int count = favoriteMapper.getFavoriteCountByUserId(userId);
            return Result.success("获取收藏总数成功", count);
        } catch (Exception e) {
            log.error("获取收藏总数时发生异常，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            return Result.error(Result.INTERNAL_ERROR,"获取收藏总数失败，请稍后重试");
        }
    }
}