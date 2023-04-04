package com.alexmisko.netty;

import org.springframework.stereotype.Component;

import com.alexmisko.config.ConditionException;
import com.alexmisko.util.TokenParseUtil;
import com.alexmisko.utils.JsonUtils;
import com.alexmisko.vo.LoginUserInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception{
        String uri = request.uri();
        log.info("uri: [{}]", uri);
        String temp [] = uri.split("/");
        String token = temp[2];
        log.info("token: [{}]", temp[2]);
        LoginUserInfo loginUserInfo = null;
        try{
            loginUserInfo = TokenParseUtil.getLoginUserInfo(token);
        } catch (Exception ex) {
            log.error("parse login user info error: [{}]", ex.getMessage(), ex);
            ctx.close();
            throw new ConditionException("token解析错误");
        }
        Long senderId = loginUserInfo.getId();
        log.info("senderId: [{}]", senderId);
        // 关联id和channel
        log.info("channelId: [{}]", ctx.channel().id().asLongText());
        UserChannelManager.userChannelGroup.put(senderId, ctx.channel());
        log.info("channel: [{}]", JsonUtils.objectToJson(ctx.channel()));
        ctx.channel().attr(AttributeKey.valueOf("id")).set(senderId);
        request.setUri("/ws");
        // 传递到下一个handler：升级握手
        ctx.fireChannelRead(request.retain());
        // 在本channel上移除这个handler消息处理，即只处理一次，鉴权通过与否
        ctx.pipeline().remove(AuthHandler.class);
    }
}