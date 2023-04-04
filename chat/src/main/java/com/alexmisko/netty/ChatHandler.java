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

import javax.annotation.Resource;

import org.apache.rocketmq.spring.core.RocketMQTemplate;

import com.alexmisko.netty.entity.MsgContent;
import com.alexmisko.netty.enums.MsgTypeEnum;
import com.alexmisko.utils.JsonUtils;
import com.alexmisko.utils.SpringUtils;

@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static RocketMQTemplate rocketMQTemplate;
    static {
        rocketMQTemplate = SpringUtils.getBean(RocketMQTemplate.class);
    }


    // 用来记录和管理所有客户端的channel
    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 当客户端连接服务端之后
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端[{}]断开", ctx.channel().id().asLongText());
        UserChannelManager.userChannelGroup.forEach((k, v) -> {
            if(v.id().asLongText().equals(ctx.channel().id().asLongText())){
                UserChannelManager.userChannelGroup.remove(k);
                log.info("移除用户id=[{}]成功！", k);
            }
        });
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
        String type = msgContent.getType();
        if (type.equals(MsgTypeEnum.WORD.type)){
            log.info("来到这了");
            // 聊天信息保存到数据库，标记未签收状态
            Long senderId = Long.valueOf(ctx.channel().attr(AttributeKey.valueOf("id")).get().toString());
            msgContent.setSenderId(senderId);
            Long receiverId = msgContent.getReceiverId();
            // 发送消息
            Channel receiverChannel = UserChannelManager.userChannelGroup.get(receiverId);
            if(receiverChannel == null){
                log.info("用户离线......");
                // 用户离线，离线消息推送至数据库
                msgContent.setStatus("offline");
                log.info("msgContent: [{}]", msgContent);
                rocketMQTemplate.convertAndSend("message_chat", msgContent);
            }else{
                Channel findChannel = users.find(receiverChannel.id());
                if(findChannel != null){
                    // 用户离线，在线消息推送至数据库
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(msgContent)));
                    log.info("chatMsg: [{}]", JsonUtils.objectToJson(msgContent));
                    msgContent.setStatus("online");
                    log.info("msgContent: [{}]", msgContent);
                    rocketMQTemplate.convertAndSend("message_chat", msgContent);
                }
            }
        }else if(type.equals(MsgTypeEnum.KEEPALIVE.type)){
            log.info("服务端收到心跳");
        }
    }
}
