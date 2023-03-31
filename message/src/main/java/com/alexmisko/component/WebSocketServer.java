package com.alexmisko.component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alexmisko.config.ConditionException;
import com.alexmisko.util.TokenParseUtil;
import com.alexmisko.vo.LoginUserInfo;

import lombok.extern.slf4j.Slf4j;

@ServerEndpoint(value = "/message/{token}")
@Component
@Slf4j
public class WebSocketServer {

    private Session session;
    private Long userId;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "token") String token){
        LoginUserInfo loginUserInfo = null;
        try{
            loginUserInfo = TokenParseUtil.getLoginUserInfo(token);
        } catch (Exception ex) {
            log.error("parse login user info error: [{}]", ex.getMessage(), ex);
            throw new ConditionException("token解析错误");
        }
        this.session = session;
        this.userId = loginUserInfo.getId();
        log.info("userId: [{}]", userId);
        log.info("this: [{}]", this);
        webSocketSet.add(this);
        try {
            sendMessage("用户ID=" + userId + "连接成功");
        } catch(IOException e){
            log.error("Websocket IO Exception");
        }
    }

    @OnClose
    public void onClose(Session session){
        for (WebSocketServer item : webSocketSet) {
            try {
                if (item.userId.equals(userId)) {
                    log.info("移除用户ID=" + userId);
                    webSocketSet.remove(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendFavorMessage(Long userId, String message) throws IOException{
        for (WebSocketServer item : webSocketSet){
            log.info("userId and session: [{}] [{}]", item.userId, item.session);
            if (item.userId.equals(userId)){
                item.sendMessage(message);
            }
        }
    }
}
