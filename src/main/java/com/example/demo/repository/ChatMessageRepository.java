package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.ChatMessage;
import com.example.demo.model.entity.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long >{
	List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
