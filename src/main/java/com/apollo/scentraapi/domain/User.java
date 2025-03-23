package com.apollo.scentraapi.domain;

import com.apollo.scentraapi.domain.common.BaseEntity;
import com.apollo.scentraapi.domain.enums.Gender;
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

    private String phoneNum;
    private String birth;

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

    public void update(String name, String password, String email, String gender) {
        if (name != null) this.name = name;
        if (password != null) this.password = password;
        if (email != null) this.email = email;
        if (gender != null) this.gender = Gender.valueOf(gender);
    }

}
