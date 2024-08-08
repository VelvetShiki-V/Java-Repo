package com.vs.blogsystem_v1.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.vs.pojo.Result;
import com.vs.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// 请求相应拦截器类
@Component      // IOC容器管理
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    @Override
    // 进入到拦截处理都是非login请求
    // 放行返回true，否则false
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("request拦截成功, 接收到用户非login请求 执行身份校验");
        // 1. 获取请求url
        log.info("用户请求url: {}, METHOD: {}", request.getRequestURL().toString(), request.getMethod());

        // 2 验证token
        String jwt = request.getHeader("token");
        if(!StringUtils.hasLength(jwt)) {
            // 非法访问，返回错误Result json
            log.error("非法请求, 没有合法token: INVALID_TOKEN\n");
            // 将返回的Result对象转为json串
            String ret = JSONObject.toJSONString(Result.error("INVALID_TOKEN"));
            // 错误信息返回，并配置对应错误码
            response.getWriter().write(ret);
            response.setStatus(401);
            return false;
        } else {
            // 4. 携带了token，开始解析jwt
            log.info("守卫请求, 开始验证jwt");
            Claims payload = JwtUtil.jwtParse(jwt);
            if(payload == null) {
                // 4.1 解析失败，token验证失败
                log.error("token验证失败: INVALID_TOKEN\n");
                String ret = JSONObject.toJSONString(Result.error("INVALID_TOKEN"));
                response.getWriter().write(ret);
                response.setStatus(401);
                return false;
            } else {
                // 4.2 解析jwt成功，放行
                log.info("请求并解析jwt成功, payload: {}, 允许放行", payload);
            }
        }
        log.info("合法token，放行进行业务处理\n");
        response.setStatus(200);        // defualt status code
        return true;
    }

    @Override
    // 业务逻辑处理后执行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle执行中...");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    // 视图渲染完毕后，最后执行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("\n\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" +
                "\n{} - {}业务执行完毕, afterCompletion..." +
                "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n\n", request.getMethod(), request.getRequestURL().toString());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
