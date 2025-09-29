package com.kingtan.users.controller;

import com.kingtan.users.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Users", description = "Operations for password reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/password/reset")
    @Operation(summary = "Request password reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) throws MessagingException {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/password/reset/confirm")
    @Operation(summary = "Reset password confirm")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successful");
    }
}
