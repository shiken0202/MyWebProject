package com.example.demo.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
public class User {

    public enum Role {
        ADMIN, SELLER, BUYER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String userName;

    @Column(nullable = false, length = 255)
    private String hashPassword;

    @Column(nullable = false, length = 100, unique = true)
    private String email;
    
    @Column(nullable = false)
    private boolean emailConfirmOK=false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "isbanned", nullable = false)
    private Boolean isBanned;

    // 一對一：一個 user 只能有一個 store
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Store store;

    // 一對多：一個 user 可以有多張訂單
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    // 一對多：一個 user 可以有多個購物車項目
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private java.util.List<CartItem> cartItems;
}