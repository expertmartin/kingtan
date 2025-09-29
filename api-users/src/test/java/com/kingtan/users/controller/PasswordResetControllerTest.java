package com.kingtan.users.controller;

import com.kingtan.users.service.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PasswordResetControllerTest {
//    @Value("${kingtan.api.users.version}")    // inject null. need to study why
//    @Value("${spring.datasource.url}")
    private String version = "v1";

    @Mock
    private PasswordResetService passwordResetService;

    @InjectMocks
    private PasswordResetController passwordResetController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(passwordResetController).build();
    }

    @Test
    void testRequestPasswordReset_Success() throws Exception {
        doNothing().when(passwordResetService).createPasswordResetToken("testuser@example.com");

        mockMvc.perform(post("/api/%s/auth/password/reset".formatted(version))
                        .param("email", "testuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset email sent"));

        verify(passwordResetService, times(1)).createPasswordResetToken("testuser@example.com");
    }

    @Test
    void testResetPassword_Success() throws Exception {
        doNothing().when(passwordResetService).resetPassword("reset-token", "newPassword");

        mockMvc.perform(post("/api/%s/auth/password/reset/confirm".formatted(version))
                        .param("token", "reset-token")
                        .param("newPassword", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful"));

        verify(passwordResetService, times(1)).resetPassword("reset-token", "newPassword");
    }
}
