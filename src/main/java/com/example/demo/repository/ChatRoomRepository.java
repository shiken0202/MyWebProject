package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.ChatRoom;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long >{
	Optional<ChatRoom>  findByBuyerAndStore(User buyer, Store store);
}
