package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.BrandLikes;
import com.apollo.scentraapi.domain.ProductLikes;
import com.apollo.scentraapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandLikesRepository extends JpaRepository<BrandLikes, Long> {
    List<BrandLikes> findAllByUser(User user);
    Optional<BrandLikes> findByUserIdAndBrandId(UUID userId, Long brandId);
}
