package com.wxs.dao;

import com.wxs.pojo.entity.StoreInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商店信息数据访问接口
 * 提供商店信息查询和状态更新的数据库操作方法
 */
@Repository
@Mapper
public interface StoreMapper {

    /**
     * 查询所有商店信息
     * 返回商店的完整信息列表，用于前端展示
     * @return 商店信息列表
     */
    @Select("SELECT id, store_name as name, address, phone, status, open_time as openTime, close_time as closeTime, created_at as createdAt, updated_at as updatedAt FROM shop")
    List<StoreInfo> selectAllStores();

    /**
     * 根据ID查询单个商店信息
     * @param id 商店ID
     * @return 商店信息
     */
    @Select("SELECT id, store_name as name, address, phone, status, open_time as openTime, close_time as closeTime, created_at as createdAt, updated_at as updatedAt FROM shop WHERE id = #{id}")
    StoreInfo selectStoreById(Integer id);

    /**
     * 更新商店营业状态
     * 只更新状态字段，同时更新修改时间确保数据一致性
     * @param id 商店ID
     * @param status 新的营业状态
     * @return 影响的行数
     */
    @Update("UPDATE shop SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStoreStatus(@Param("id") Integer id, @Param("status") String status);

    /**
     * 更新商店完整信息
     * 用于管理员修改商店基本信息
     * @param storeInfo 商店信息对象
     * @return 影响的行数
     */
    @Update("UPDATE shop SET store_name = #{name}, address = #{address}, phone = #{phone}, " +
            "open_time = #{openTime}, close_time = #{closeTime}, updated_at = NOW() WHERE id = #{id}")
    int updateStoreInfo(StoreInfo storeInfo);
}