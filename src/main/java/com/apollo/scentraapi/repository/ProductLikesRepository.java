package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.ProductLikes;
import com.apollo.scentraapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductLikesRepository extends JpaRepository<ProductLikes, Long> {
    List<ProductLikes> findAllByUser(User user);
    Optional<ProductLikes> findByProductId(Long productId);
}