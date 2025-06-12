package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.exception.StoreNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.ChatRoomMapper;
import com.example.demo.model.dto.ChatMessageDto;
import com.example.demo.model.dto.ChatRoomDto;
import com.example.demo.model.entity.ChatRoom;
import com.example.demo.model.entity.Store;
import com.example.demo.model.entity.User;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.ChatMessageService;
import com.example.demo.service.ChatRoomService;

@Controller
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ChatController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private ChatMessageService chatMessageService;
	@Autowired
	private ChatRoomRepository chatRoomRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private ChatRoomService chatRoomService;
	@Autowired
	private ChatRoomMapper chatRoomMapper;
	@MessageMapping("/chatRoom/{buyerId}/{storeId}")
	public void getAndSendMessage(@DestinationVariable Long buyerId,@DestinationVariable Long storeId ,@Payload ChatMessageDto dto) {
		User buyer=userRepository.findById(buyerId)
				.orElseThrow(()->new UserNotFoundException("找不到買家"));
		Store store=storeRepository.findById(storeId)
				.orElseThrow(()->new StoreNotFoundException("找不到賣場"));
		ChatRoom chatRoom = chatRoomRepository.findByBuyerAndStore(buyer,store)
		        .orElseGet(() -> 
		            chatRoomService.createChatRoom(
		                buyerId, 
		                storeId
		            )
		        );
		ChatMessageDto responseDto=chatMessageService.saveMessage(dto.getSenderId(),chatRoom,dto.getContent());
	
		simpMessagingTemplate.convertAndSend("/topic/messages/"+buyerId.toString()+"_"+storeId.toString(),responseDto);
	}
	@GetMapping("/chat/{roomId}")
	  public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getMessage(@PathVariable("roomId") Long roomId){
	    List<ChatMessageDto> chatMessageDtos = chatMessageService.getMessagesByChatRoom(roomId);
	    return ResponseEntity.ok(ApiResponse.success("取得訊息成功",chatMessageDtos));
	  }
	@GetMapping("/chat/room")
	public ResponseEntity<ApiResponse<ChatRoomDto>> getChatRoom(
	    @RequestParam Long buyerId,
	    @RequestParam Long storeId
	) {
	    try {
	        User buyer = userRepository.findById(buyerId).orElseThrow();
	        Store store = storeRepository.findById(storeId).orElseThrow();
	        
	        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByBuyerAndStore(buyer, store);
	        
	        if (chatRoomOpt.isPresent()) {
	        	ChatRoom chatRoom=chatRoomOpt.get();
	        	
	            return ResponseEntity.ok(ApiResponse.success("查詢聊天室成功",chatRoomMapper.toDto(chatRoom)));
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    } catch (Exception e) {
	        return ResponseEntity.notFound().build();
	    }
	}

	
}
