package com.alexmisko.config;

import com.alexmisko.constant.CommonConstant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Slf4j
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(RequestContextHolder.getRequestAttributes() != null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);
            log.info("token: [{}]", token);
            requestTemplate.header(CommonConstant.JWT_USER_INFO_KEY, token);
        }else {
            requestTemplate.header(CommonConstant.JWT_USER_INFO_KEY, "rocketmq-vip-access");
        }
    }
}
