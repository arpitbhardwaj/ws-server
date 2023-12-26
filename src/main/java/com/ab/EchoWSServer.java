package com.ab;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo-ws-server")
@ApplicationScoped
public class EchoWSServer {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen > " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose > " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("onError > " + session.getId() + ": " + throwable);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("onMessage > " + message);
    }
}
