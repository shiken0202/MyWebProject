package com.example.demo.model.entity;
import java.math.BigDecimal;
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
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 // 多對一：多個商品對一家 store
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

 // 多對一：多個商品對一個分類
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 255)
    private String brand;

    @Column(length = 255)
    private String stock;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(length=255)
    private String description;
    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive=false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
 // 一對多：一個商品有多個圖片
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

    // 一對多：一個商品有多個訂單明細
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    // 一對多：一個商品有多個購物車項目
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    // Getter/Setter ...
}
