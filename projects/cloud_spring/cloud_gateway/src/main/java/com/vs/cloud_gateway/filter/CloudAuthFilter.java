package com.vs.cloud_gateway.filter;

import cn.hutool.json.JSONUtil;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_common.exception.CustomException;
import com.vs.cloud_common.utils.JwtUtil;
import com.vs.cloud_gateway.config.AuthProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudAuthFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    private final StringRedisTemplate template;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 网关权限过滤器，仅当访问权限路径时需要进入此逻辑并进行身份校验，并附加额外的请求头信息进入微服务内部进行用户身份处理
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("\n-----------------token filter拦截-----------------");
        // 登录拦截
        ServerHttpRequest request = exchange.getRequest();
        // 判断是否需要路径拦截
        if(isExcluded(request.getURI().getRawPath())) {
            // 直接放行
            return chain.filter(exchange);
        }
        // 权限路径拦截校验
        HttpHeaders headers = request.getHeaders();
        List<String> list = headers.get("Authorization");
        if(list == null || list.size() == 0) {
            log.error("gateway拦截: 不存在合法token，认证失败");
            // 设置响应内容，将Result对象转换为JSON字符串
            String json = JSONUtil.toJsonStr(Result.error("gateway拦截: 不存在合法token，认证失败"));
            // 将 JSON 字符串转换为 DataBuffer
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(buffer));
        }
        // 获取到token，开始解析
        String token = list.get(0);
        Map<String, Object> userMap = null;
        try {
            userMap = JwtUtil.jwtParseRefresh(template, token);
        } catch (Exception e) {
            // token解析失败，身份校验失败
            log.error("gateway拦截: 非法token解析失败");
            String json = JSONUtil.toJsonStr(Result.error("gateway拦截: 非法token解析失败"));
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.writeWith(Mono.just(buffer));
        }
        // 用户身份校验成功，从token payload解析用户并添加额外的请求头信息
        log.info("成功解析出用户授权信息: {}, 放行权限路径", userMap);
        Map<String, Object> finalUserMap = userMap;
        exchange.mutate().request(builder -> builder
                .header("uid", finalUserMap.get("uid").toString())
                .header("name", finalUserMap.get("name").toString())
                .build());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    // 判断路径权限
    boolean isExcluded(String path) {
        for(String pathPattern: authProperties.getExcludePaths()) {
            // 匹配到exclude path则放行
            log.info("遍历非权限路径{}-->path: {}", pathPattern, path);
            if(pathMatcher.match(pathPattern, path)) {
                log.info("匹配成功, 直接放行");
                return true;
            }
        }
        // 其他则为权限路径
        log.info("匹配失败，需要进行身份验证");
        return false;
    }
}
