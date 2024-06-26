package org.ict.intelligentclass.payment.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CART")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_seq")
    @SequenceGenerator(name = "cart_seq", sequenceName = "SQ_CART_ID", allocationSize = 1)
    @Column(name = "CART_ID")
    private Long cartId;

    @Column(name = "USEREMAIL", nullable = false)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false)
    private String provider;
}
