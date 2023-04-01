package com.alexmisko.controller;

import com.alexmisko.config.ConditionException;
import com.alexmisko.constant.SmsConstant;
import com.alexmisko.pojo.User;
import com.alexmisko.service.JwtService;
import com.alexmisko.util.SendSmsCodeUtil;
import com.alexmisko.vo.JwtToken;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JwtService jwtService;


    @PostMapping("signIn")
    public Result<JwtToken> signIn(@RequestBody User user) throws Exception{
        log.info("sign in with param [{}] [{}]", user.getUsername(), user.getPassword());
        return jwtService.signIn(user);
    }

    @PostMapping("signUp/{code}")
    public Result<JwtToken> signUp(@PathVariable String code, @RequestBody User user) throws Exception{
        log.info("sign up with param [{}] [{}]", user.getUsername(), user.getPassword());
        return jwtService.signUp(user, code);
    }

    @GetMapping(value = {"smsCode/{phoneNumber}", "smsCode/"})
    public Result<String> sendSMSCode(@PathVariable(required = false) String phoneNumber){
        // 先检测手机号
        if(Objects.isNull(phoneNumber)){
            throw new ConditionException("手机号不能为空！");
        }
        String regex = "^[1]\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new ConditionException("无效的手机号");
        }
        // 控制发送频率
        Object object = redisTemplate.getExpire(SmsConstant.MOBILE_SMS_CODE + ":" + phoneNumber, TimeUnit.SECONDS);
        if (object != null && (600 - Integer.parseInt(object.toString()) < 60)){
            throw new ConditionException("请求频率过快！");
        }
        String code = SendSmsCodeUtil.generateCode();
//        if(SendSmsCodeUtil.sendSmsCode(phoneNumber, code).equals("OK")){
//            log.info("短信发送成功，手机号和验证码：[{}] [{}]", phoneNumber, code);
//        }
        log.info("短信发送成功，手机号和验证码：[{}] [{}]", phoneNumber, code);
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("miss", 0);
        redisTemplate.opsForHash().putAll(SmsConstant.MOBILE_SMS_CODE + ":" + phoneNumber, map);
        redisTemplate.expire(SmsConstant.MOBILE_SMS_CODE + ":" + phoneNumber, 600, TimeUnit.SECONDS);
        log.info("redis存储成功");
        return Result.success("短信发送成功，十分钟内有效！");
    }

    @GetMapping("user/{userId}")
    public Result<User> getUser(@PathVariable Long userId) throws Exception{
        return jwtService.getUser(userId);
    }
}
