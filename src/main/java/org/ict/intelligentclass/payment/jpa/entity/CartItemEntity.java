package org.ict.intelligentclass.payment.jpa.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CART_ITEM")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_seq")
    @SequenceGenerator(name = "cart_item_seq", sequenceName = "SQ_CART_ITEM_ID", allocationSize = 1)
    @Column(name = "CART_ITEM_ID")
    private Long cartItemId;

    @Column(name = "CART_ID", nullable = false)
    private Long cartId;

    @Column(name = "LECTURE_PACKAGE_ID", nullable = false)
    private Long lecturePackageId;

    @Column(name = "DATE_ADDED", nullable = false)
    private LocalDateTime dateAdded;
}
