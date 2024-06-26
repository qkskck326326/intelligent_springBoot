package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCartId(Long cartId);


    void deleteByCartItemIdIn(List<Long> ids);
}
