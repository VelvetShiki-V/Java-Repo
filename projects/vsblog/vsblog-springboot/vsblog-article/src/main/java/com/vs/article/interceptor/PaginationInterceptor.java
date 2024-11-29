package com.vs.article.interceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vs.framework.utils.PageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

import static com.vs.article.constant.GlobalConstant.*;

// 分页拦截器
@Slf4j
@Component
public class PaginationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求页码和大小
        String currentPage = request.getParameter(CURRENT);
        String pageSize = request.getParameter(SIZE);
        if(Objects.nonNull(currentPage) && Objects.nonNull(pageSize)) {
            // 初始化线程页对象
            PageUtil.setPageHolder(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        PageUtil.removePageHolder();
    }
}
