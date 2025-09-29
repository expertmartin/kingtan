package com.kingtan.users.repository;

import com.kingtan.users.model.PasswordResetToken;
import com.kingtan.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PasswordResetTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordResetToken testToken;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Create and persist a User
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        entityManager.persist(testUser);

        // Create and persist a PasswordResetToken
        testToken = new PasswordResetToken();
        testToken.setToken("reset-token-123");
        testToken.setUser(testUser);
        testToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Expires in 1 hour
        entityManager.persistAndFlush(testToken);
    }

    @Test
    void findByToken_shouldReturnTokenWhenTokenExists() {
        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findByToken("reset-token-123");

        assertTrue(foundToken.isPresent(), "Token should be found by token string");
        assertEquals("reset-token-123", foundToken.get().getToken(), "Token string should match");
        assertEquals(testUser.getId(), foundToken.get().getUser().getId(), "User ID should match");
        assertNotNull(foundToken.get().getExpiryDate(), "Expiry date should not be null");
    }

    @Test
    void findByToken_shouldReturnEmptyWhenTokenDoesNotExist() {
        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findByToken("non-existent-token");

        assertFalse(foundToken.isPresent(), "No token should be found for non-existent token");
    }

    @Test
    void findByExpiryDateBefore_shouldReturnExpiredTokens() {
        // Create a second User for the expired token
        User secondUser = new User();
        secondUser.setUsername("secondUser");
        secondUser.setEmail("second@example.com");
        secondUser.setPassword("password");
        secondUser.setEnabled(true);
        entityManager.persist(secondUser);

        // Create an expired token for the second User
        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setToken("expired-token-456");
        expiredToken.setUser(secondUser);
        expiredToken.setExpiryDate(LocalDateTime.now().minusHours(1)); // Expired 1 hour ago
        entityManager.persist(expiredToken);

        // Create a third User for the non-expired token
        User thirdUser = new User();
        thirdUser.setUsername("thirdUser");
        thirdUser.setEmail("third@example.com");
        thirdUser.setPassword("password");
        thirdUser.setEnabled(true);
        entityManager.persist(thirdUser);

        // Create a non-expired token for the third User
        PasswordResetToken nonExpiredToken = new PasswordResetToken();
        nonExpiredToken.setToken("non-expired-token-789");
        nonExpiredToken.setUser(thirdUser);
        nonExpiredToken.setExpiryDate(LocalDateTime.now().plusHours(2)); // Expires in 2 hours
        entityManager.persistAndFlush(nonExpiredToken);

        List<PasswordResetToken> expiredTokens = passwordResetTokenRepository.findByExpiryDateBefore(LocalDateTime.now());

        assertEquals(1, expiredTokens.size(), "Should find one expired token");
        assertEquals("expired-token-456", expiredTokens.get(0).getToken(), "Expired token string should match");
    }

    @Test
    void findByExpiryDateBefore_shouldReturnEmptyWhenNoTokensExpired() {
        // Ensure testToken is not expired
        testToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        entityManager.persistAndFlush(testToken);

        List<PasswordResetToken> expiredTokens = passwordResetTokenRepository.findByExpiryDateBefore(LocalDateTime.now().minusHours(1));

        assertTrue(expiredTokens.isEmpty(), "No tokens should be found when none are expired");
    }

    @Test
    void save_shouldPersistNewToken() {
        // Delete the existing token to allow a new token for testUser
        passwordResetTokenRepository.deleteById(testToken.getId());
        entityManager.flush();

        PasswordResetToken newToken = new PasswordResetToken();
        newToken.setToken("new-token-999");
        newToken.setUser(testUser);
        newToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        PasswordResetToken savedToken = passwordResetTokenRepository.save(newToken);
        entityManager.flush();

        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findById(savedToken.getId());
        assertTrue(foundToken.isPresent(), "Saved token should be found by ID");
        assertEquals("new-token-999", foundToken.get().getToken(), "Token string should match");
        assertEquals(testUser.getId(), foundToken.get().getUser().getId(), "User ID should match");
    }

    @Test
    void save_shouldThrowExceptionForDuplicateToken() {
        PasswordResetToken duplicateToken = new PasswordResetToken();
        duplicateToken.setToken("reset-token-123"); // Same as testToken
        duplicateToken.setUser(testUser);
        duplicateToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        assertThrows(DataIntegrityViolationException.class, () -> {
            passwordResetTokenRepository.save(duplicateToken);
            entityManager.flush();
        }, "Should throw exception for duplicate token");
    }

    @Test
    void findById_shouldReturnTokenWhenIdExists() {
        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findById(testToken.getId());

        assertTrue(foundToken.isPresent(), "Token should be found by ID");
        assertEquals("reset-token-123", foundToken.get().getToken(), "Token string should match");
    }

    @Test
    void findById_shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findById(999L);

        assertFalse(foundToken.isPresent(), "No token should be found for non-existent ID");
    }

    @Test
    void existsById_shouldReturnTrueWhenIdExists() {
        boolean exists = passwordResetTokenRepository.existsById(testToken.getId());

        assertTrue(exists, "Token ID should exist");
    }

    @Test
    void existsById_shouldReturnFalseWhenIdDoesNotExist() {
        boolean exists = passwordResetTokenRepository.existsById(999L);

        assertFalse(exists, "Non-existent token ID should not exist");
    }

    @Test
    void deleteById_shouldRemoveTokenWhenIdExists() {
        passwordResetTokenRepository.deleteById(testToken.getId());
        entityManager.flush();

        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findById(testToken.getId());
        assertFalse(foundToken.isPresent(), "Token should be deleted");
    }

    @Test
    void deleteById_shouldDoNothingWhenIdDoesNotExist() {
        passwordResetTokenRepository.deleteById(999L);
        entityManager.flush();

        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findByToken("reset-token-123");
        assertTrue(foundToken.isPresent(), "Existing token should remain unaffected");
    }
}