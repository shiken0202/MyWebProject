package com.example.demo.model.entity;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 多對一：多個訂單明細對一張訂單
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // 多對一：多個訂單明細對一個商品
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(name = "product_title_snapshot", nullable = false)
    private String productTitleSnapshot;

    @Column(name = "product_brand_snapshot")
    private String productBrandSnapshot;

    @Column(name = "product_price_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal productPriceSnapshot;
    
}
