package com.chatapp.chatapp_backend.repository;

import com.chatapp.chatapp_backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
        Long senderId1, Long receiverId1,
        Long senderId2, Long receiverId2
);

List<Message> findByGroupIdOrderByTimestampAsc(Long groupId);
}