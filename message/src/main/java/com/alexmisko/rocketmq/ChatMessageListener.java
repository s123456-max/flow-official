package com.alexmisko.rocketmq;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import com.alexmisko.pojo.MsgContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "chat-consumer", topic = "message_chat")
public class ChatMessageListener implements RocketMQListener<MsgContent>{

    @Override
    public void onMessage(MsgContent msgContent){
        log.info("message: [{}]", msgContent);
    }
}
