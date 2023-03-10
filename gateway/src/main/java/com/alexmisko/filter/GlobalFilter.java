package com.alexmisko.filter;

import com.alexmisko.constant.CommonConstant;
import com.alexmisko.util.TokenParseUtil;
import com.alexmisko.vo.LoginUserInfo;
import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import com.alexmisko.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class GlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    @Autowired
    ResourceUtil resourceUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获取请求
        ServerHttpRequest request = exchange.getRequest();

        // 获取响应
        ServerHttpResponse response = exchange.getResponse();

        log.info("请求路径和参数是 [{}] [{}]", request.getURI().getPath(), request.getQueryParams());

        String requestPath = request.getURI().getPath();

        // 访问公共资源则直接放行
        if(resourceUtil.isResourcePath(requestPath, resourceUtil.getPath("ROLE_PUBLIC"))){
            log.info("访问公共资源：[{}]", requestPath);
            return chain.filter(exchange);
        }

        //不是登录或注册，校验token是否能解析
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo = null;
        try {
            loginUserInfo = TokenParseUtil.getLoginUserInfo(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("request with role [{}]", JSON.toJSONString(loginUserInfo));
        if (null == loginUserInfo || !resourceUtil.isResourcePath(requestPath, resourceUtil.getPath(loginUserInfo.getRole()))){
            response.getHeaders().add(HttpHeaders.CONTENT_TYPE,"application/json;charset=UTF-8");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            String body = "{\"code\":403,\"msg\":\"没有权限！\"}";
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer).doOnError(error -> DataBufferUtils.release(buffer)));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
