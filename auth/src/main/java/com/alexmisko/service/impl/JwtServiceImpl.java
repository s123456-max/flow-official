package com.alexmisko.service.impl;

import com.alexmisko.config.ConditionException;
import com.alexmisko.constant.AuthConstant;
import com.alexmisko.constant.CommonConstant;
import com.alexmisko.constant.SmsConstant;
import com.alexmisko.dao.UserMapper;
import com.alexmisko.pojo.User;
import com.alexmisko.service.JwtService;
import com.alexmisko.util.MD5Util;
import com.alexmisko.util.RSAUtil;
import com.alexmisko.util.TokenParseUtil;
import com.alexmisko.vo.JwtToken;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JwtServiceImpl implements JwtService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 生成token
     */
    @Override
    public String generateToken(User user) throws Exception{
        LoginUserInfo loginUserInfo = new LoginUserInfo(user.getId(), user.getUsername(), user.getRole());
        ZonedDateTime zdt = LocalDate.now().plus(AuthConstant.DEFAULT_EXPIRE_DAY, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());
        return Jwts.builder().claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                .setId(UUID.randomUUID().toString())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.RS256, getPrivateKey())
                .compact();
    }

    /**
     * 登录
     */
    @Override
    public Result<JwtToken> signIn(User user) throws Exception {
        String username = user.getUsername();
        String regex = "^[1]\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new ConditionException("无效的手机号！");
        }
        String rawPassword = user.getPassword();
        if(StringUtils.isNullOrEmpty(rawPassword)){
            throw new ConditionException("密码不能为空！");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user_query = userMapper.selectOne(queryWrapper);
        if(user_query == null){
            throw new ConditionException("用户不存在！");
        }
        log.info("user sign in with username and rawPassword: [{}] [{}]", username, rawPassword);
        String password = RSAUtil.decrypt(rawPassword);
        log.info("decrypted password: [{}]", password);
        String salt = user_query.getSalt();
        String MD5Password = user_query.getPassword();
        log.info("queried MD5Password: [{}]", MD5Password);
        String generatedPassword = MD5Util.sign(password, salt, "UTF-8");
        log.info("generated MD5Password: [{}]", generatedPassword);
        // 开始比对
        if(!MD5Password.equals(generatedPassword)){
            throw new ConditionException("密码错误！");
        }
        log.info("request with role: [{}]", user_query.getRole());
        return Result.success("登陆成功！", new JwtToken(generateToken(user_query)));
    }

    /**
     * 注册
     */
    @Override
    public Result<JwtToken> signUp(User user, String code) throws Exception {
        String regex = "^[1]\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(user.getUsername());
        if (!matcher.matches()) {
            throw new ConditionException("无效的手机号！");
        }
        if(StringUtils.isNullOrEmpty(user.getPassword())){
            throw new ConditionException("密码不能为空！");
        }
        if(!redisTemplate.hasKey(SmsConstant.MOBILE_SMS_CODE + ":" + user.getUsername())){
            throw new ConditionException("验证码已过期！");
        }
        Object object = redisTemplate.opsForHash().get(SmsConstant.MOBILE_SMS_CODE + ":" + user.getUsername(), "code");
        String code_query = object.toString();
        log.info("user and redis smsCode：[{}] [{}]", code, code_query);
        if(!code.equals(code_query)){
            Long num = redisTemplate.opsForHash().increment(SmsConstant.MOBILE_SMS_CODE + ":" + user.getUsername(), "miss", 1);
            // 输入错误三次，更改成一个不可能参数，防止暴力破解，一个小时内不能再次请求发送验证码
            if(num == 3){
                redisTemplate.opsForHash().put(SmsConstant.MOBILE_SMS_CODE + ":" + user.getUsername(), "code", "alex");
                redisTemplate.expire(SmsConstant.MOBILE_SMS_CODE + ":" + user.getUsername(), 3600, TimeUnit.SECONDS);
                throw new ConditionException("已达最大错误次数，冻结1小时！");
            }
            throw new ConditionException("验证码无效！");
        }
        // 验证码验证通过后，及时删除
        redisTemplate.delete(SmsConstant.MOBILE_SMS_CODE + ":" + user.getUsername());
        // 前面为验证部分，下面开始密码处理
        String password = RSAUtil.decrypt(user.getPassword());
        log.info("decrypted password: [{}]", password);
        // 生成时间戳，作为MD5加密的盐
        String salt = String.valueOf(new Date().getTime());
        log.info("generated salt: [{}]", salt);
        String MD5Password = MD5Util.sign(password, salt, "UTF-8");
        log.info("generated MD5Password: [{}]", MD5Password);
        // 下面查询是否存在该用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User user_query = userMapper.selectOne(queryWrapper);
        // 密码以MD5格式存储
        user.setPassword(MD5Password);
        // 一定要存储盐，解密的关键
        user.setSalt(salt);
        // 添加角色
        user.setRole("ROLE_USER");
        if(user_query != null){
            log.info("username is registered: [{}]", user_query);
            userMapper.update(user, queryWrapper);
            return Result.success("重置密码成功！", new JwtToken(generateToken(user)));
        }
        userMapper.insert(user);
        log.info("register user success: [{}] [{}]", user.getUsername(), user.getId());
        return Result.success("注册成功！", new JwtToken(generateToken(user)));
    }

    /**
     * 私钥格式转换
     **/
    private PrivateKey getPrivateKey() throws Exception{
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
