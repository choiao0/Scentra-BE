package com.apollo.scentraapi.repository;

import com.apollo.scentraapi.domain.Seller;
import com.apollo.scentraapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Optional<Seller> findByUser(User user);
}
