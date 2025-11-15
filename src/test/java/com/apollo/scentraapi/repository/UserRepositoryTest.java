package com.apollo.scentraapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.apollo.scentraapi.domain.User;
import com.apollo.scentraapi.domain.enums.Gender;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = createUser("user", "user@example.com", Gender.MALE);

        userRepository.save(user);
    }

    @Test
    @DisplayName("존재하는 이메일로 유저를 조회하면 Optional을 반환한다.")
    void findByEmail_existing() {
        // when
        Optional<User> foundUser = userRepository.findByEmail("user@example.com");

        // then
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getEmail()).isEqualTo("user@example.com");
                    assertThat(user.getName()).isEqualTo("user");
                    assertThat(user.getGender()).isEqualTo(Gender.MALE);
                });
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 유저를 조회하면 반환 값이 비어있다.")
    void findByEmail_notFound() {
        // when
        Optional<User> foundUser = userRepository.findByEmail("noone@example.com");

        // then
        assertThat(foundUser).isEmpty();
    }

    private User createUser(String name, String email, Gender gender){
        return User.builder()
                .name(name)
                .email(email)
                .gender(gender)
                .build();
    }
}
