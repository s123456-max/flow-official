package com.alexmisko.component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@ServerEndpoint(value = "/message/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    private Session session;
    private Long userId;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketServer = new CopyOnWriteArraySet<WebSocketServer>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId){
        this.session = session;
        this.userId = userId;
        log.info("userId: [{}]", userId);
        log.info("this: [{}]", this);
        webSocketServer.add(this);
        try {
            sendMessage("用户ID:" + userId + "连接成功");
        } catch(IOException e){
            log.error("Websocket IO Exception");
        }
    }

    public void sendMessage(String message) throws IOException {
        log.info("session: {[]}", this.session);
        this.session.getBasicRemote().sendText(message);
    }
}
