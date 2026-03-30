package com.chatapp.chatapp_backend.controller;

import com.chatapp.chatapp_backend.model.Message;
import com.chatapp.chatapp_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 🔹 Send Message
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        return chatService.sendMessage(message);
    }

    // 🔹 Get Messages
    @GetMapping("/messages")
    public List<Message> getMessages(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {

        return chatService.getMessages(senderId, receiverId);
    }

    @GetMapping("/group")
public List<Message> getGroupMessages(@RequestParam Long groupId) {
    return chatService.getGroupMessages(groupId);
}
}