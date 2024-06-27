package org.ict.intelligentclass.payment.model.service;

import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.payment.jpa.entity.CartEntity;
import org.ict.intelligentclass.payment.jpa.entity.CartItemEntity;
import org.ict.intelligentclass.payment.jpa.repository.CartItemRepository;
import org.ict.intelligentclass.payment.jpa.repository.CartRepository;
import org.ict.intelligentclass.payment.jpa.repository.PaymentRepository;
import org.ict.intelligentclass.payment.model.dto.CartItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private LecturePackageRepository lecturePackageRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public String  addPackageToCart(String userEmail, String provider, Long lecturePackageId) {
        CartEntity cart = cartRepository.findByUserEmailAndProvider(userEmail, provider)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUserEmail(userEmail);
                    newCart.setProvider(provider);
                    return cartRepository.save(newCart);
                });

        boolean alreadyExists = cartItemRepository.existsByCartIdAndLecturePackageId(cart.getCartId(), lecturePackageId);

        if (alreadyExists) {
            return "EXISTS";
        } else {
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setCartId(cart.getCartId());
            cartItem.setLecturePackageId(lecturePackageId);
            if (cartItem.getDateAdded() == null) {
                cartItem.setDateAdded(LocalDateTime.now());
            }
            cartItemRepository.save(cartItem);
            return "ADDED";
        }
    }

    public List<CartItemDto> getCartItems(Long cartId, String userEmail) {
        List<Long> purchasedPackageIds = paymentRepository.findPurchasedPackageIdsByUserEmail(userEmail);
        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cartId);
        List<CartItemDto> cartItemDTOs = new ArrayList<>();

        for (CartItemEntity cartItem : cartItems) {
            if (purchasedPackageIds.contains(cartItem.getLecturePackageId())) {
                continue;
            }
            LecturePackageEntity lecturePackage = lecturePackageRepository.findById(cartItem.getLecturePackageId()).orElseThrow();
            CartItemDto dto = new CartItemDto();
            dto.setLecturePackageId(lecturePackage.getLecturePackageId());
            dto.setTitle(lecturePackage.getTitle());
            dto.setPrice(lecturePackage.getPriceForever()); // 가격 설정
            dto.setThumbnail(lecturePackage.getThumbnail());
            dto.setNickname(lecturePackage.getNickname());
            dto.setContent(lecturePackage.getContent());
            cartItemDTOs.add(dto);
        }

        return cartItemDTOs;
    }

    public CartEntity getOrCreateCart(String userEmail, String provider) {
        return cartRepository.findByUserEmailAndProvider(userEmail, provider)
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setUserEmail(userEmail);
                    newCart.setProvider(provider);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public void deleteCartItems(Long cartId, List<Long> lecturePackageIds) {
        cartItemRepository.deleteByCartIdAndLecturePackageIdIn(cartId, lecturePackageIds);
    }



}
