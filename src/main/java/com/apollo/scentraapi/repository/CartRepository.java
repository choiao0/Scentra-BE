package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // 특정 유저의 장바구니 목록 조회
    List<Cart> findByUserId(UUID userId);
    // 특정 유저의 특정 상품 조회
    Optional<Cart> findByUserIdAndProductId(UUID userId, Long productId);

    // ✅ 특정 유저의 특정 상품 삭제 (장바구니에서 개별 삭제)
    void deleteByUserIdAndProductId(UUID userId, Long productId);

    // ✅ 특정 유저의 장바구니 전체 삭제
    void deleteByUserId(UUID userId);
}

