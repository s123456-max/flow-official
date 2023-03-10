package com.alexmisko.config;

import com.alexmisko.vo.Result;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public Result<String> commonExceptionHandler(HttpServletRequest request, Exception e){
        String errorMsg = e.getMessage();
        if(e instanceof ConditionException){
            String errorCode = ((ConditionException)e).getCode();
            return Result.fail(errorCode, errorMsg);
        }else{
            return Result.fail("500", errorMsg);
        }
    }
}
