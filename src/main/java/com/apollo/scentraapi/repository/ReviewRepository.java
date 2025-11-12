package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Product;
import com.apollo.scentraapi.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProduct(Product product);
}
