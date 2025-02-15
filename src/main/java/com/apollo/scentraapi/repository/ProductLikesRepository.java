package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.ProductLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikesRepository extends JpaRepository<ProductLikes, Long> {
}