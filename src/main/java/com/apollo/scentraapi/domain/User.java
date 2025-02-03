package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import com.apollo.scentraapi.domain.enums.Gender;
import com.apollo.scentraapi.dto.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ProductLikes> productLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> cartList = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Seller seller;

    @Override
    public String getUsername() {
        return this.name;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public void update(UserRequest.UserUpdateDTO request) {
        if (request.getName() != null) this.name = request.getName();
        if (request.getPassword() != null) this.password = request.getPassword();
        if (request.getEmail() != null) this.email = request.getEmail();
        if (request.getGender() != null) this.gender = Gender.valueOf(request.getGender());
    }

}
