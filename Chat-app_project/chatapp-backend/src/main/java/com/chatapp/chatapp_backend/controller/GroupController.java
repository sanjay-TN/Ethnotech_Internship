package com.chatapp.chatapp_backend.controller;

import com.chatapp.chatapp_backend.model.ChatGroup;
import com.chatapp.chatapp_backend.repository.ChatGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final ChatGroupRepository groupRepository;

    @PostMapping("/create")
    public ChatGroup createGroup(@RequestBody ChatGroup group) {
        return groupRepository.save(group);
    }

    @GetMapping("/all")
    public List<ChatGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    
}