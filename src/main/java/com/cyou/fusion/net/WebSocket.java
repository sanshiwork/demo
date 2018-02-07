package com.cyou.fusion.net;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocket
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
@ServerEndpoint(value = "/websocket")
@Component
public class WebSocket {

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
    }


    @OnError
    public void onError(Session session, Throwable throwable) {

    }
}
