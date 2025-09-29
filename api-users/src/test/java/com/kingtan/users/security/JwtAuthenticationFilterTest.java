package com.kingtan.users.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String testUsername = "testUser";
    private final String validToken = "validToken";
    private final String bearerToken = "Bearer " + validToken;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext(); // Clear SecurityContext before each test
    }

    @Test
    void doFilterInternal_shouldBypassAuthForRegisterEndpoint() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/users/register");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldBypassAuthForLoginEndpoint() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldBypassAuthForPasswordResetEndpoint() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/auth/password/reset");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldBypassAuthForPasswordResetConfirmEndpoint() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/auth/password/reset/confirm");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldAuthenticateWithValidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(validToken)).thenReturn(testUsername);
        when(userDetailsService.loadUserByUsername(testUsername)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).validateToken(validToken);
        verify(jwtUtil).getUsernameFromToken(validToken);
        verify(userDetailsService).loadUserByUsername(testUsername);
        verify(filterChain).doFilter(request, response);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        assertNotNull(securityContext.getAuthentication());
        assertTrue(securityContext.getAuthentication() instanceof UsernamePasswordAuthenticationToken);
        assertEquals(userDetails, securityContext.getAuthentication().getPrincipal());
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWithInvalidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(validToken)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).validateToken(validToken);
        verifyNoInteractions(userDetailsService);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWithoutBearerPrefix() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn("Invalid " + validToken);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(jwtUtil, userDetailsService);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldNotAuthenticateWithNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(jwtUtil, userDetailsService);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldHandleUserNotFoundException() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(validToken)).thenReturn(testUsername);
        when(userDetailsService.loadUserByUsername(testUsername)).thenThrow(new UsernameNotFoundException("User not found"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).validateToken(validToken);
        verify(jwtUtil).getUsernameFromToken(validToken);
        verify(userDetailsService).loadUserByUsername(testUsername);
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}