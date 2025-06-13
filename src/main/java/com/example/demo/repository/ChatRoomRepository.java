package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.ChatRoom;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long >{
	Optional<ChatRoom>  findByBuyerAndStore(User buyer, Store store);

	@Query("SELECT c FROM ChatRoom c WHERE c.buyer.id = :userId OR c.seller.id = :userId")
	List<ChatRoom> findAllChatRoomsByUserId(@Param("userId") Long userId);
}
