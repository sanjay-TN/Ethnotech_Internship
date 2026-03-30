package com.chatapp.chatapp_backend.repository;

import com.chatapp.chatapp_backend.model.ChatGroup;
// import com.chatapp.chatapp_backend.model.Message;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    
}