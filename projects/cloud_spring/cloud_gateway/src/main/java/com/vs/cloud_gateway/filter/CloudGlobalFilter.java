package com.vs.cloud_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CloudGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("\n\n-----------------global filter拦截-----------------");
        ServerHttpRequest request = exchange.getRequest();
        log.info("接收到用户请求url: {}, method: {}", request.getURI(), request.getMethod());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // int类型，值越小优先级越高
        // 最后一个过滤器为netty routing filter优先级最低，为Integer.Max
        return 0;
    }
}
