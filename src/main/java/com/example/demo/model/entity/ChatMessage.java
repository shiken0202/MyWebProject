package com.example.demo.model.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_messages")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 
	// 所屬聊天室
    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    // 發送者
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    

    // 文字內容
    @Column(columnDefinition = "TEXT")
    private String content;

   
    // 是否已讀
    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
