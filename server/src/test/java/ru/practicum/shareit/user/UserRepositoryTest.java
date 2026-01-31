package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("user")
                .email("user@mail.ru")
                .build();
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    void saveUser_shouldGenerateId() {
        User newUser = User.builder()
                .name("New User")
                .email("new@mail.ru")
                .build();

        User saved = userRepository.save(newUser);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("New User");
        assertThat(saved.getEmail()).isEqualTo("new@mail.ru");
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        User found = userRepository.findById(user.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(user.getId());
        assertThat(found.getName()).isEqualTo("user");
        assertThat(found.getEmail()).isEqualTo("user@mail.ru");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        boolean exists = userRepository.findById(999L).isPresent();
        assertThat(exists).isFalse();
    }
}