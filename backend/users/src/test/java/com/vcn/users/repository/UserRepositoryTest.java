package com.vcn.users.repository;

import com.vcn.users.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mariadb.MariaDBContainer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer mariaDBContainer = new MariaDBContainer("mariadb:10.11");

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_withValidUsername_returnsOptionalUser() {
        User user = new User();
        user.setUsername("vicente@gmail.com");
        user.setPassword("password");
        testEntityManager.persistAndFlush(user);
        Optional<User> userOptional = userRepository.findByUsername("vicente@gmail.com");
        assertThat(userOptional).isPresent();
        assertThat(userOptional.get().getUsername()).isEqualTo("vicente@gmail.com");
    }
}
