package org.ict.intelligentclass.payment.model.service;

import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.jpa.repository.CouponRepositoy;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class paymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepositoy couponRepositoy;

    public List<CouponEntity> getCouponsByUserEmail(String userEmail) {
//        userRepository.findByEmail(userEmail);
        CouponEntity couponEntity = new CouponEntity();
        return couponRepositoy.findByUserEmail(userEmail);

    }
}
