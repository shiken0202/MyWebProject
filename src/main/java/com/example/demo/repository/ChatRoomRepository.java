package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long >{

}
