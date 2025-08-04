package com.example.ecommerce.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, updatable = true)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Double totalAmount;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = true)
    private PaymentMethod paymentMethod;

    public enum PaymentMethod{
        BANK_TRANSFER,
        QRIS,
        CASH_ON_DELIVERY,
    }


    public enum OrderStatus{
        PENDING,
        PAID,
        WAITING_PAYMENT,
    }

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updateAt  = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }


}
