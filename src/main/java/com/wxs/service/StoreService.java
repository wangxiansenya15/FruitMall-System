package com.wxs.service;

import com.wxs.dao.StoreMapper;
import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.StoreInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 商店信息服务类
 * 提供商店信息查询和状态管理的业务逻辑
 */
@Service
@Slf4j
public class StoreService {

    @Autowired
    private StoreMapper storeMapper;

    /**
     * 查询所有商店信息
     * 返回系统中所有商店的基本信息
     * @return 包含商店信息列表的结果对象
     */
    public Result<List<StoreInfo>> getAllStores() {
        try {
            log.info("开始查询所有商店信息");
            List<StoreInfo> stores = storeMapper.selectAllStores();
            log.info("查询到{}家商店信息", stores.size());
            return Result.success(stores);
        } catch (Exception e) {
            log.error("查询商店信息失败", e);
            return Result.ServerError("查询商店信息失败");
        }
    }

    /**
     * 根据ID查询单个商店信息
     * @param id 商店ID
     * @return 包含商店信息的结果对象
     */
    public Result<StoreInfo> getStoreById(Integer id) {
        if (id == null || id <= 0) {
            log.warn("查询商店信息时ID参数无效: {}", id);
            return Result.error(Result.BAD_REQUEST, "商店ID不能为空且必须大于0");
        }

        try {
            log.info("开始查询商店信息，ID: {}", id);
            StoreInfo store = storeMapper.selectStoreById(id);
            if (store == null) {
                log.warn("未找到ID为{}的商店信息", id);
                return Result.error(Result.NOT_FOUND, "未找到指定的商店信息");
            }
            log.info("成功查询到商店信息: {}", store.getName());
            return Result.success(store);
        } catch (Exception e) {
            log.error("查询商店信息失败，ID: {}", id, e);
            return Result.ServerError("查询商店信息失败");
        }
    }

    /**
     * 更新商店营业状态
     * 支持状态的实时更新，确保数据持久化
     * @param id 商店ID
     * @param status 新的营业状态
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> updateStoreStatus(Integer id, String status) {
        // 参数校验
        if (id == null || id <= 0) {
            log.warn("更新商店状态时ID参数无效: {}", id);
            return Result.error(Result.BAD_REQUEST, "商店ID不能为空且必须大于0");
        }

        if (!StringUtils.hasText(status)) {
            log.warn("更新商店状态时状态参数为空");
            return Result.error(Result.BAD_REQUEST, "商店状态不能为空");
        }

        // 验证状态值是否合法
        if (!isValidStatus(status)) {
            log.warn("无效的商店状态: {}", status);
            return Result.error(Result.BAD_REQUEST, "无效的商店状态，支持的状态：营业中、休息中、节假日休息、系统维护中");
        }

        try {
            log.info("开始更新商店状态，ID: {}, 新状态: {}", id, status);
            
            // 先检查商店是否存在
            StoreInfo existingStore = storeMapper.selectStoreById(id);
            if (existingStore == null) {
                log.warn("要更新状态的商店不存在，ID: {}", id);
                return Result.error(Result.NOT_FOUND, "要更新的商店不存在");
            }

            // 执行状态更新
            int updatedRows = storeMapper.updateStoreStatus(id, status);
            if (updatedRows > 0) {
                log.info("成功更新商店状态，ID: {}, 状态: {} -> {}", id, existingStore.getStatus(), status);
                return Result.success("商店状态更新成功");
            } else {
                log.error("更新商店状态失败，没有行被更新，ID: {}", id);
                return Result.ServerError( "更新商店状态失败");
            }
        } catch (Exception e) {
            log.error("更新商店状态时发生异常，ID: {}, 状态: {}", id, status, e);
            return Result.ServerError("更新商店状态失败");
        }
    }

    /**
     * 验证商店状态是否合法
     * 根据数据库表定义的ENUM值进行验证
     * @param status 待验证的状态
     * @return 是否为合法状态
     */
    private boolean isValidStatus(String status) {
        return "营业中".equals(status) || 
               "休息中".equals(status) || 
               "节假日休息".equals(status) || 
               "系统维护中".equals(status);
    }
}