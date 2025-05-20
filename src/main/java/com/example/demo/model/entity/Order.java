package com.example.demo.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 多對一：多張訂單對一個 user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	// 多對一：多張訂單對一家 store
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
	
	@Column(name="total_amount" ,nullable=false,precision=10,scale=2)
	private BigDecimal totalAmount;
	
	@Column(nullable = false,length = 20)
	private String status;
	
	@Column(name="created_at",nullable=false)
	private LocalDateTime createdAt;
	
	@Column(name="updated_at",nullable=false)
	private LocalDateTime updateAt;
	
	@Column(nullable=false,length = 255)
	private String address;
	
	@Column(name = "payment_type", nullable = false, length = 50)
    private String paymentType;

    // 一對多：一張訂單有多個訂單明細
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

}
