package com.example.puppytalk.Shop;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String impUid;
    private String merchantUid;
    private int amount;
    private String payMethod;
    private String status;

    @Builder
    public Payment(Order order, String impUid, String merchantUid, int amount, String payMethod, String status) {
        this.order = order;
        this.impUid = impUid;
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.payMethod = payMethod;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}