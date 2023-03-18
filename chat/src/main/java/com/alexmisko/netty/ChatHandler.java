package com.alexmisko.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用来记录和管理所有客户端的channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 当客户端连接服务端之后
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开");
        System.out.println(ctx.channel().id().asLongText());
        System.out.println(ctx.channel().id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        System.out.println("接收到的数据：" + content);
        clients.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接收到消息，消息为：" + content));
    }
}
