package com.wxs.service;

import com.wxs.config.ImageStorageConfig;
import com.wxs.dao.ProfileMapper;
import com.wxs.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class ImageStorageService {
    private final Path FileStorageLocation;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    public ImageStorageService(ImageStorageConfig fileStorageProperties) {
        this.FileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.FileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("无法创建文件存储目录", ex);
        }
    }

    // 存储文件
    public Result<String> storeFile(MultipartFile file,Integer id) throws IOException {
        // 检查文件名是否为空
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error(Result.BAD_REQUEST,"文件名不能为空！");
        }

        // 规范化文件名
        String fileName = StringUtils.cleanPath(originalFilename);

        try {
            // 检查文件名是否包含非法字符
            if(fileName.contains("..")) {
                log.error("文件名包含无效的路径序列 {}", fileName);
                throw new RuntimeException("抱歉! 文件名包含无效的路径序列 " + fileName);
            }

            // 复制文件到目标位置(替换同名文件)
            Path targetLocation = this.FileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


            String networkFileStorage_URL = "http://120.24.248.110/avatars/";
            String newAvatarUrl = networkFileStorage_URL + fileName;
            String currentAvatarUrl = profileMapper.selectAvatarById(id);

            // 仅当新头像与当前头像不同时才处理删除
            if (!newAvatarUrl.equals(currentAvatarUrl)) {
                // 从URL中提取文件名
                String currentFileName = currentAvatarUrl.substring(currentAvatarUrl.lastIndexOf('/') + 1);
                Path currentFilePath = this.FileStorageLocation.resolve(currentFileName);

                // 安全删除旧头像（非默认头像）
                if (!currentAvatarUrl.equals("http://120.24.248.110/avatars/default_images.jpg")) {
                    try {
                        Files.deleteIfExists(currentFilePath);
                        log.info("删除旧头像文件: {}", currentFileName);
                    } catch (IOException e) {
                        log.error("无法删除旧头像文件: {}", currentFileName, e);
                    }
                }
            }

            // 更新数据库（无论是否同名都需要更新，因为文件可能被覆盖）
            profileMapper.updateAvatar(id, newAvatarUrl);
            log.info("网络🛜存储路径: {}", networkFileStorage_URL + fileName);
            log.info("文件存储成功: {}", fileName);
            log.info("头像更新成功: {}", newAvatarUrl);
            return Result.success("文件存储成功! ", networkFileStorage_URL + fileName);
        } catch (IOException ex) {
            log.error("文件存储失败: {}", fileName);
            throw new IOException("无法存储文件 " + fileName + ". 请重试!", ex);
        }
    }

    @Deprecated //  弃用 原上传头像方法
    public Result<String> oldStoreFile(MultipartFile file,Integer id) throws IOException {
        // 检查文件名是否为空
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error(Result.BAD_REQUEST,"文件名不能为空！");
        }

        // 规范化文件名
        String fileName = StringUtils.cleanPath(originalFilename);

        try {
            // 检查文件名是否包含非法字符
            if(fileName.contains("..")) {
                log.error("文件名包含无效的路径序列 {}", fileName);
                throw new RuntimeException("抱歉! 文件名包含无效的路径序列 " + fileName);
            }

            // 复制文件到目标位置(替换同名文件)
            Path targetLocation = this.FileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String networkFileStorage_URL = "http://120.24.248.110/avatars/";
            String databaseURL = profileMapper.selectAvatarById(id);
            if(delOriginalFile(databaseURL, "/Users/wxs-mac/Desktop/uploads/" + databaseURL.substring(databaseURL.lastIndexOf('/') + 1))){
                profileMapper.updateAvatar(id, networkFileStorage_URL + fileName);// 更新用户头像
            }
            //log.info("网络🛜存储路径: {}", networkFileStorage_URL + fileName);
            //log.info("文件存储成功: {}", fileName);
            return Result.success("文件存储成功! ", networkFileStorage_URL + fileName);
        } catch (IOException ex) {
            //log.error("文件存储失败: {}", fileName);
            throw new IOException("无法存储文件 " + fileName + ". 请重试!", ex);
        }
    }
    @Deprecated
    private boolean delOriginalFile(String path, String fileName) {
        Path filePath = Paths.get(fileName);
        log.info("原始文件路径: {}", filePath);
        if (!isDeleteFile(path)) {
            return false;
        }else {
            log.info("删除文件: {}", filePath);
            try {
                Files.deleteIfExists(filePath);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("无法删除文件: " + filePath, e);
            }
        }
    }

    @Deprecated
    private boolean isDeleteFile(String path) {
        return !path.equals("http://120.24.248.110/avatars/default_images.jpg");
    }
}