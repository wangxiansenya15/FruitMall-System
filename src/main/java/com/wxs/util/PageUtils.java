package com.wxs.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageUtils {
    /**
     * 开始分页
     * @param page  页码
     * @param size 每页显示数量
     */
    public static void startPage(int page, int size) {
        PageHelper.startPage(page, size);
    }

    /**
     * 获取分页信息
     */
    public static <T> PageInfo<T> getPageInfo(List<T> list) {
        return new PageInfo<>(list);
    }
}
