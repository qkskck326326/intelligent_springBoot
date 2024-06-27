package org.ict.intelligentclass.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.payment.jpa.entity.CartEntity;
import org.ict.intelligentclass.payment.jpa.repository.CartItemRepository;
import org.ict.intelligentclass.payment.model.dto.CartItemDto;
import org.ict.intelligentclass.payment.model.service.CartService;
import org.ict.intelligentclass.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cart")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin //리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class CartController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemRepository cartItemRepository;

//    @GetMapping("/items")
//    public ResponseEntity<List<CartItemDto>> getCartItems(@RequestParam Long cartId) {
//        List<CartItemDto> cartItems = cartService.getCartItems(cartId);
//        return ResponseEntity.ok(cartItems);
//    }
//
//    @PostMapping("/init")
//    public ResponseEntity<CartEntity> initializeCart(@RequestParam String userEmail, @RequestParam String provider) {
//        CartEntity cart = cartService.getOrCreateCart(userEmail, provider);
//        return ResponseEntity.ok(cart);
//    }

    @GetMapping("/{userEmail}/{provider}")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable String userEmail, @PathVariable String provider) {
        CartEntity cart = cartService.getOrCreateCart(userEmail, provider);
        List<CartItemDto> cartItems = cartService.getCartItems(cart.getCartId(), userEmail);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCartItems(@RequestBody List<Long> ids) {
        cartService.deleteCartItems(ids);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/items")
//    public ResponseEntity<?> deleteCartItems(@RequestBody List<Long> itemIds) {
//        try {
//            cartService.deleteCartItemsAll(itemIds);
//            return ResponseEntity.ok("Cart items deleted successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting cart items");
//        }
//    }
}
