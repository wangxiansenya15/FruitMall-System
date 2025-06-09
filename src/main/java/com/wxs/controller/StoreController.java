package com.wxs.controller;

import com.wxs.pojo.dto.Result;
import com.wxs.pojo.entity.StoreInfo;
import com.wxs.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商店信息控制器
 * 提供商店信息查询和状态管理的REST API接口
 */
@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {

    @Autowired
    private StoreService storeService;

    /**
     * 查询所有商店信息
     * GET /store/list
     * 返回系统中所有商店的基本信息，用于前端展示商店列表
     * @return 包含商店信息列表的响应结果
     */
    @GetMapping("/list")
    public Result<List<StoreInfo>> getAllStores() {
        log.info("接收到查询所有商店信息的请求");
        return storeService.getAllStores();
    }

    /**
     * 根据ID查询单个商店信息
     * GET /store/{id}
     * 根据商店ID查询详细的商店信息
     * @param id 商店ID
     * @return 包含商店信息的响应结果
     */
    @GetMapping("/{id}")
    public Result<StoreInfo> getStoreById(@PathVariable Integer id) {
        log.info("接收到查询商店信息的请求，ID: {}", id);
        return storeService.getStoreById(id);
    }

    /**
     * 更新商店营业状态
     * PUT /store/{id}/status
     * 更新指定商店的营业状态，支持实时状态切换
     * @param id 商店ID
     * @param requestBody 包含新状态的请求体
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStoreStatus(@PathVariable Integer id, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        log.info("接收到更新商店状态的请求，ID: {}, 新状态: {}", id, status);
        return storeService.updateStoreStatus(id, status);
    }

    /**
     * 获取商店状态选项
     * GET /store/status-options
     * 返回系统支持的所有商店状态选项，用于前端下拉选择
     * @return 包含状态选项的响应结果
     */
    @GetMapping("/status-options")
    public Result<List<String>> getStatusOptions() {
        log.info("接收到查询商店状态选项的请求");
        List<String> statusOptions = List.of(
            "营业中",
            "休息中", 
            "节假日休息",
            "系统维护中"
        );
        return Result.success("获取状态选项成功", statusOptions);
    }

    /**
     * 获取当前商店营业状态
     * GET /store/current-status
     * 获取第一家商店的当前营业状态，用于前端显示当前营业状态
     * 适用于单店铺系统
     * @return 包含当前状态的响应结果
     */
    @GetMapping("/current-status")
    public Result<Map<String, Object>> getCurrentStatus() {
        log.info("接收到查询当前商店状态的请求");
        try {
            Result<List<StoreInfo>> storesResult = storeService.getAllStores();
            if (storesResult.getCode() == Result.OK &&
                storesResult.getData() != null && 
                !storesResult.getData().isEmpty()) {
                
                StoreInfo firstStore = storesResult.getData().get(0);
                Map<String, Object> statusInfo = Map.of(
                    "id", firstStore.getId(),
                    "name", firstStore.getName(),
                    "status", firstStore.getStatus(),
                    "openTime", firstStore.getOpenTime() != null ? firstStore.getOpenTime().toString() : null,
                    "closeTime", firstStore.getCloseTime() != null ? firstStore.getCloseTime().toString() : null
                );
                return Result.success("获取当前商店状态成功",  statusInfo);
            } else {
                return Result.error(Result.NOT_FOUND, "未找到商店信息");
            }
        } catch (Exception e) {
            log.error("获取当前商店状态失败", e);
            return Result.ServerError("获取当前商店状态失败");
        }
    }
}