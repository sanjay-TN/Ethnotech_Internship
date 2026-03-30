package com.chatapp.chatapp_backend.websocket;

import com.chatapp.chatapp_backend.model.Message;
import com.chatapp.chatapp_backend.model.User;
import com.chatapp.chatapp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final UserRepository userRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {

        message.setTimestamp(LocalDateTime.now());

        // 🔥 FETCH USERNAME
        User user = userRepository.findById(message.getSenderId())
                .orElse(null);

        if (user != null) {
            message.setSenderName(user.getUsername());
        }

        return message;
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public Map<String, String> typing(Map<String, String> data) {
        return data;
    }

    @MessageMapping("/online")
    @SendTo("/topic/online")
    public Map<String, Object> online(Map<String, Object> data) {
        return data;
    }
}