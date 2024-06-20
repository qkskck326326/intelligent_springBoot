package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
