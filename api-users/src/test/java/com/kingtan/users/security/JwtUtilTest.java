package com.kingtan.users.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String testSecret = "testSecretKey12345678901234567890123456789012345678901234567890123456789012345678901234567890";
    private final long testExpiration = 3600000; // 1 hour in milliseconds
    private final String testUsername = "testUser";

    @BeforeEach
    void setUp() {
        // Inject test values for private fields
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", testExpiration);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtUtil.generateToken(testUsername);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verify token components
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // Header, Payload, Signature

        // Verify token can be parsed
        String username = Jwts.parser()
                .setSigningKey(testSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals(testUsername, username);
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String token = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + testExpiration))
                .signWith(SignatureAlgorithm.HS512, testSecret)
                .compact();

        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals(testUsername, username);
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String token = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + testExpiration))
                .signWith(SignatureAlgorithm.HS512, testSecret)
                .compact();

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidSignature() {
        String invalidToken = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + testExpiration))
                .signWith(SignatureAlgorithm.HS512, "12345678901234567890123456789012345678901234567890123456789012345678901234567890wrongSecret")
                .compact();

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date(System.currentTimeMillis() - testExpiration - 1000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, testSecret)
                .compact();

        assertFalse(jwtUtil.validateToken(expiredToken));
    }

    @Test
    void validateToken_shouldReturnFalseForMalformedToken() {
        String malformedToken = "invalid.token.string";
        assertFalse(jwtUtil.validateToken(malformedToken));
    }

    @Test
    void validateToken_shouldReturnFalseForNullToken() {
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    void getUsernameFromToken_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.string";
        assertThrows(Exception.class, () -> jwtUtil.getUsernameFromToken(invalidToken));
    }
}