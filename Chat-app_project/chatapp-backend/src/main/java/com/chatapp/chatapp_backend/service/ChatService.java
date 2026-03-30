package com.chatapp.chatapp_backend.service;

import com.chatapp.chatapp_backend.model.Message;
import com.chatapp.chatapp_backend.model.User;
import com.chatapp.chatapp_backend.repository.MessageRepository;
import com.chatapp.chatapp_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository; // 🔥 ADD THIS

    // 🔹 Send message
    public Message sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());

        // 🔥 SET sender name
        User user = userRepository.findById(message.getSenderId())
                .orElse(null);

        if (user != null) {
            message.setSenderName(user.getUsername());
        }

        return messageRepository.save(message);
    }

    // 🔹 Private chat messages
    public List<Message> getMessages(Long senderId, Long receiverId) {

        List<Message> messages = messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                        senderId, receiverId,
                        receiverId, senderId
                );

        // 🔥 Attach sender names
        for (Message msg : messages) {
            User user = userRepository.findById(msg.getSenderId())
                    .orElse(null);

            if (user != null) {
                msg.setSenderName(user.getUsername());
            }
        }

        return messages;
    }

    // 🔹 Group chat messages
    public List<Message> getGroupMessages(Long groupId) {

        List<Message> messages =
                messageRepository.findByGroupIdOrderByTimestampAsc(groupId);

        // 🔥 Attach sender names (IMPORTANT FIX)
        for (Message msg : messages) {
            User user = userRepository.findById(msg.getSenderId())
                    .orElse(null);

            if (user != null) {
                msg.setSenderName(user.getUsername());
            }
        }

        return messages;
    }
}