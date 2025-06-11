package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long >{

}
