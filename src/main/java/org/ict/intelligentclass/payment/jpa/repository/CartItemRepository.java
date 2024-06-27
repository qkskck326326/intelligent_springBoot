package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.CartEntity;
import org.ict.intelligentclass.payment.jpa.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCartId(Long cartId);


//    void deleteByLecturePackageIdIn(List<Long> ids);

    boolean existsByCartIdAndLecturePackageId(Long cartId, Long lecturePackageId);

    void deleteByCartIdAndLecturePackageIdIn(Long cartId, List<Long> lecturePackageIds);
}
