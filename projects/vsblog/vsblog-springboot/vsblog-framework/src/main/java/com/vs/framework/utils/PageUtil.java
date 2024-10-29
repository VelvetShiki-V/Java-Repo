package com.vs.framework.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

public class PageUtil {
    private static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();

    // 设置page线程对象
    public static void setPageHolder(Page page) { PAGE_HOLDER.set(page); }

    // 获取page线程对象
    public static Page getPageHolder() {
        Page page = PAGE_HOLDER.get();
        if(Objects.isNull(page)) setPageHolder(new Page());
        return PAGE_HOLDER.get();
    }

    // 获取当前页码
    public static Long getCurrentPage() { return getPageHolder().getCurrent(); }

    // 获取每页条目个数
    public static Long getPageSize() { return getPageHolder().getSize(); }

    // 获取页偏移（计算数据库查询的下一页起始值）
    public static Long getPageOffset() { return (getCurrentPage() - 1) * getPageSize(); }

    // 移除page对象
    public static void removePageHolder() { PAGE_HOLDER.remove(); }
}
