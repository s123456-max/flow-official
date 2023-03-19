package com.alexmisko.netty;

import org.springframework.stereotype.Component;

import com.alexmisko.utils.JsonUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if (msg instanceof FullHttpRequest){
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpHeaders headers = request.headers();
            String token = headers.get("alexmisko-user");
            log.info("token: [{}]", token);
            Long senderId = Long.valueOf(token);
            log.info("senderId: [{}]", senderId);
            // 关联id和channel
            UserChannelManager.userChannelGroup.put(senderId, ctx.channel());
            log.info("channel: [{}]", JsonUtils.objectToJson(ctx.channel()));
            ctx.pipeline().remove(this);
            ctx.channel().attr(AttributeKey.valueOf("id")).set(senderId);
            ctx.fireChannelRead(msg);
        }
    }
}