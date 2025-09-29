package com.kingtan.users.repository;

import com.kingtan.users.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    private Role testRole;

    @BeforeEach
    void setUp() {
        // Create and persist a Role
        testRole = new Role();
        testRole.setName("ROLE_USER");
        entityManager.persistAndFlush(testRole);
    }

    @Test
    void findByName_shouldReturnRoleWhenNameExists() {
        Optional<Role> foundRole = roleRepository.findByName("ROLE_USER");

        assertTrue(foundRole.isPresent(), "Role should be found by name");
        assertEquals("ROLE_USER", foundRole.get().getName(), "Role name should match");
        assertNotNull(foundRole.get().getId(), "Role ID should not be null");
    }

    @Test
    void findByName_shouldReturnEmptyWhenNameDoesNotExist() {
        Optional<Role> foundRole = roleRepository.findByName("ROLE_NONEXISTENT");

        assertFalse(foundRole.isPresent(), "No role should be found for non-existent name");
    }

    @Test
    void save_shouldPersistNewRole() {
        Role newRole = new Role();
        newRole.setName("ROLE_ADMIN");

        Role savedRole = roleRepository.save(newRole);
        entityManager.flush();

        Optional<Role> foundRole = roleRepository.findById(savedRole.getId());
        assertTrue(foundRole.isPresent(), "Saved role should be found by ID");
        assertEquals("ROLE_ADMIN", foundRole.get().getName(), "Role name should match");
    }

    @Test
    void save_shouldThrowExceptionForDuplicateRoleName() {
        Role duplicateRole = new Role();
        duplicateRole.setName("ROLE_USER"); // Same as testRole

        assertThrows(DataIntegrityViolationException.class, () -> {
            roleRepository.save(duplicateRole);
            entityManager.flush();
        }, "Should throw exception for duplicate role name");
    }

    @Test
    void findById_shouldReturnRoleWhenIdExists() {
        Optional<Role> foundRole = roleRepository.findById(testRole.getId());

        assertTrue(foundRole.isPresent(), "Role should be found by ID");
        assertEquals("ROLE_USER", foundRole.get().getName(), "Role name should match");
    }

    @Test
    void findById_shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<Role> foundRole = roleRepository.findById(999L);

        assertFalse(foundRole.isPresent(), "No role should be found for non-existent ID");
    }

    @Test
    void existsById_shouldReturnTrueWhenIdExists() {
        boolean exists = roleRepository.existsById(testRole.getId());

        assertTrue(exists, "Role ID should exist");
    }

    @Test
    void existsById_shouldReturnFalseWhenIdDoesNotExist() {
        boolean exists = roleRepository.existsById(999L);

        assertFalse(exists, "Non-existent role ID should not exist");
    }

    @Test
    void deleteById_shouldRemoveRoleWhenIdExists() {
        roleRepository.deleteById(testRole.getId());
        entityManager.flush();

        Optional<Role> foundRole = roleRepository.findById(testRole.getId());
        assertFalse(foundRole.isPresent(), "Role should be deleted");
    }

    @Test
    void deleteById_shouldDoNothingWhenIdDoesNotExist() {
        roleRepository.deleteById(999L);
        entityManager.flush();

        // No exception should be thrown, and no changes to existing data
        Optional<Role> foundRole = roleRepository.findByName("ROLE_USER");
        assertTrue(foundRole.isPresent(), "Existing role should remain unaffected");
    }
}