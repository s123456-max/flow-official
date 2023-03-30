package com.alexmisko.rocketmq;

import java.io.IOException;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.component.WebSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "message_favor")
public class FavorMessageListener implements RocketMQListener<String>{

    @Autowired
    private WebSocketServer webSocketServer;
    @Override
    public void onMessage(String message){
        log.info("message: [{}]", message);
        // try {
        //     webSocketServer.sendMessage("hello");
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
