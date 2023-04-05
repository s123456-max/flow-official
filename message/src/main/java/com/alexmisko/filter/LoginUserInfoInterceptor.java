package com.alexmisko.filter;


import com.alexmisko.config.ConditionException;
import com.alexmisko.constant.CommonConstant;
import com.alexmisko.util.TokenParseUtil;
import com.alexmisko.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户身份统一登录拦截
 */
@Slf4j
@Component
public class LoginUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String token = request.getHeader(CommonConstant.JWT_USER_INFO_KEY);
        if(token == null){
            throw new ConditionException("token为空");
        }
        LoginUserInfo loginUserInfo = null;
        try{
            loginUserInfo = TokenParseUtil.getLoginUserInfo(token);
        } catch (Exception ex) {
            log.error("parse login user info error: [{}]", ex.getMessage(), ex);
            throw new ConditionException("token解析错误");
        }
        log.info("set login user info: [{}]", request.getRequestURI());
        AccessContext.setLoginUserInfo(loginUserInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
        if(null != AccessContext.getLoginUserInfo()){
            AccessContext.clearLoginUserInfo();
        }
    }
}
