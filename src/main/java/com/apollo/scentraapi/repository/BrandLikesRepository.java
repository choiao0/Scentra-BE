package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Brand;
import com.apollo.scentraapi.domain.BrandLikes;
import com.apollo.scentraapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandLikesRepository extends JpaRepository<BrandLikes, Long> {
    List<BrandLikes> findAllByUser(User user);
    @Query("select bl from BrandLikes bl join fetch bl.brand")
    List<BrandLikes> findAllByUserWithBrand(User user);
    Optional<BrandLikes> findByUserAndBrand(User user, Brand brand);
}
