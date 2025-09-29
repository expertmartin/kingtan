package com.kingtan.users.service;

import com.kingtan.users.model.PasswordResetToken;
import com.kingtan.users.model.User;
import com.kingtan.users.repository.PasswordResetTokenRepository;
import com.kingtan.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.mail.internet.MimeMessage;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User user;
    private PasswordResetToken token;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("oldPassword");

        token = new PasswordResetToken();
        token.setId(1L);
        token.setToken("reset-token");
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
    }

    @Test
    void testCreatePasswordResetToken_Success() throws Exception {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(user));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(token);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        passwordResetService.createPasswordResetToken("testuser@example.com");

        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testCreatePasswordResetToken_UserNotFound() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> passwordResetService.createPasswordResetToken("testuser@example.com"));
        verify(tokenRepository, never()).save(any(PasswordResetToken.class));
    }

    @Test
    void testResetPassword_Success() {
        when(tokenRepository.findByToken("reset-token")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        passwordResetService.resetPassword("reset-token", "newPassword");

        verify(userRepository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).delete(token);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void testResetPassword_InvalidToken() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> passwordResetService.resetPassword("invalid-token", "newPassword"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testResetPassword_ExpiredToken() {
        token.setExpiryDate(LocalDateTime.now().minusHours(1));
        when(tokenRepository.findByToken("reset-token")).thenReturn(Optional.of(token));

        assertThrows(RuntimeException.class, () -> passwordResetService.resetPassword("reset-token", "newPassword"));
        verify(userRepository, never()).save(any(User.class));
    }
}
