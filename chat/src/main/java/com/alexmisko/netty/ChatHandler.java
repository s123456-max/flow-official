package com.alexmisko.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.alexmisko.netty.entity.MsgContent;
import com.alexmisko.netty.enums.MsgTypeEnum;
import com.alexmisko.utils.JsonUtils;

@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用来记录和管理所有客户端的channel
    private static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 当客户端连接服务端之后
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开");
        System.out.println(ctx.channel().id().asLongText());
        System.out.println(ctx.channel().id().asShortText());
        users.remove(ctx.channel());
    }

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常以后关闭channel，随后从channelgroup中移除
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        log.info(JsonUtils.objectToJson(content));
        MsgContent msgContent = JsonUtils.jsonToObject(content, MsgContent.class);
        Integer type = msgContent.getType();
        if (type == MsgTypeEnum.CONNECT.type){
            log.info("CONNECT连接");
        }else if(type == MsgTypeEnum.CHAT.type){
            // 聊天信息保存到数据库，标记未签收状态
            Long senderId = Long.valueOf(ctx.channel().attr(AttributeKey.valueOf("id")).get().toString());
            msgContent.setSenderId(senderId);
            Long receiverId = msgContent.getReceiverId();
            String msgId = "msg-0001";
            msgContent.setMsgId(msgId);
            // 发送消息
            Channel receiverChannel = UserChannelManager.userChannelGroup.get(receiverId);
            if(receiverChannel == null){
                log.info("用户离线......");
                // 用户离线，消息推送
            }else{
                Channel findChannel = users.find(receiverChannel.id());
                if(findChannel != null){
                    // 用户在线
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(msgContent)));
                    log.info("chatMsg: [{}]", JsonUtils.objectToJson(msgContent));
                }
            }
        }else if(type == MsgTypeEnum.SIGNED.type){

        }else if(type == MsgTypeEnum.KEEPALIVE.type){

        }
    }
}
