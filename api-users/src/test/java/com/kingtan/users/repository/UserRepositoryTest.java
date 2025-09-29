package com.kingtan.users.repository;

import com.kingtan.users.model.Role;
import com.kingtan.users.model.User;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        // Create and persist a Role
        testRole = new Role();
        testRole.setName("ROLE_USER");
        entityManager.persist(testRole);

        // Create and persist a User with the Role
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        roles.add(testRole);
        testUser.setRoles(roles);

        entityManager.persistAndFlush(testUser);
    }

    @Test
    void findByUsername_shouldReturnUserWhenUsernameExists() {
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertTrue(foundUser.isPresent(), "User should be found by username");
        assertEquals("testUser", foundUser.get().getUsername(), "Username should match");
        assertEquals("test@example.com", foundUser.get().getEmail(), "Email should match");
        assertTrue(foundUser.get().isEnabled(), "User should be enabled");
        assertEquals(1, foundUser.get().getRoles().size(), "User should have one role");
        assertEquals("ROLE_USER", foundUser.get().getRoles().iterator().next().getName(), "Role name should match");
    }

    @Test
    void findByUsername_shouldReturnEmptyWhenUsernameDoesNotExist() {
        Optional<User> foundUser = userRepository.findByUsername("nonExistent");

        assertFalse(foundUser.isPresent(), "No user should be found for non-existent username");
    }

    @Test
    void findByEmail_shouldReturnUserWhenEmailExists() {
        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent(), "User should be found by email");
        assertEquals("test@example.com", foundUser.get().getEmail(), "Email should match");
        assertEquals("testUser", foundUser.get().getUsername(), "Username should match");
        assertTrue(foundUser.get().isEnabled(), "User should be enabled");
        assertEquals(1, foundUser.get().getRoles().size(), "User should have one role");
        assertEquals("ROLE_USER", foundUser.get().getRoles().iterator().next().getName(), "Role name should match");
    }

    @Test
    void findByEmail_shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent(), "No user should be found for non-existent email");
    }

    @Test
    void existsByUsername_shouldReturnTrueWhenUsernameExists() {
        boolean exists = userRepository.existsByUsername("testUser");

        assertTrue(exists, "Username should exist");
    }

    @Test
    void existsByUsername_shouldReturnFalseWhenUsernameDoesNotExist() {
        boolean exists = userRepository.existsByUsername("nonExistent");

        assertFalse(exists, "Non-existent username should not exist");
    }

    @Test
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        boolean exists = userRepository.existsByEmail("test@example.com");

        assertTrue(exists, "Email should exist");
    }

    @Test
    void existsByEmail_shouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists, "Non-existent email should not exist");
    }

    @Test
    void save_shouldPersistUserWithRoles() {
        Role newRole = new Role();
        newRole.setName("ROLE_ADMIN");
        entityManager.persist(newRole);

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("newPassword");
        newUser.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        roles.add(newRole);
        newUser.setRoles(roles);

        User savedUser = userRepository.save(newUser);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent(), "Saved user should be found by ID");
        assertEquals("newUser", foundUser.get().getUsername(), "Username should match");
        assertEquals("new@example.com", foundUser.get().getEmail(), "Email should match");
        assertEquals(1, foundUser.get().getRoles().size(), "User should have one role");
        assertEquals("ROLE_ADMIN", foundUser.get().getRoles().iterator().next().getName(), "Role name should match");
    }

    @Test
    void save_shouldThrowExceptionForDuplicateUsername() {
        User duplicateUser = new User();
        duplicateUser.setUsername("testUser"); // Same as testUser
        duplicateUser.setEmail("different@example.com");
        duplicateUser.setPassword("password");
        duplicateUser.setEnabled(true);
        duplicateUser.setRoles(new HashSet<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(duplicateUser);
            entityManager.flush();
        }, "Should throw exception for duplicate username");
    }

    @Test
    void save_shouldThrowExceptionForDuplicateEmail() {
        User duplicateUser = new User();
        duplicateUser.setUsername("differentUser");
        duplicateUser.setEmail("test@example.com"); // Same as testUser
        duplicateUser.setPassword("password");
        duplicateUser.setEnabled(true);
        duplicateUser.setRoles(new HashSet<>());

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(duplicateUser);
            entityManager.flush();
        }, "Should throw exception for duplicate email");
    }

    @Test
    void save_shouldThrowExceptionForDuplicateRoleName() {
        Role duplicateRole = new Role();
        duplicateRole.setName("ROLE_USER"); // Same as testRole

        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(duplicateRole);
            entityManager.flush();
        }, "Should throw exception for duplicate role name");
    }
}