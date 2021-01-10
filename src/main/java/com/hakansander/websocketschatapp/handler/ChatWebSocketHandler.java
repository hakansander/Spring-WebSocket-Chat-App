package com.hakansander.websocketschatapp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakansander.websocketschatapp.model.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        UserMessage userMessage = objectMapper.readValue(message.getPayload(), UserMessage.class);

        log.info("[handleTextMessage] A user sent a message:: user={}, message={}", userMessage.getUser(), userMessage.getMessage());

        for(WebSocketSession webSocketSession : webSocketSessions){
            webSocketSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session);
    }
}
