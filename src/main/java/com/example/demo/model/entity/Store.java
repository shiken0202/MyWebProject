package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stores")
@EntityListeners(AuditingEntityListener.class)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 // 一對一：一個 store 對應一個 user，user_id 唯一
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(columnDefinition = "TEXT")
    private String description;
    
 
 // 一對多：一個 store 可以有多個商品
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Product> products;

    // 一對多：一個 store 可以有多張訂單
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Order> orders;
    // Getter/Setter ...
}
