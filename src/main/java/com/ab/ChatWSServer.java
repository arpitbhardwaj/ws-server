package com.ab;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat-ws-server/{username}")
@ApplicationScoped
public class ChatWSServer {

    Map<String, Session> sessions = new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String name) {
        System.out.println("onOpen > " + name);
        sessions.put(name, session);
        broadcast("User " + name + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String name) {
        System.out.println("onClose > " + name);
        sessions.remove(name);
        broadcast("User " + name + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String name, Throwable throwable) {
        System.out.println("onError > " + name + ": " + throwable);
        sessions.remove(name);
        broadcast("User " + name + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String name) {
        System.out.println("onMessage > " + name + ": " + message);
        broadcast(">> " + name + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result ->  {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
