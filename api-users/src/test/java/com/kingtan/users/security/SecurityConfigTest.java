package com.kingtan.users.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // We're using ReflectionTestUtils to set the final fields
        // because @InjectMocks and @Mock don't handle final fields directly in the constructor.
        ReflectionTestUtils.setField(securityConfig, "jwtUtil", jwtUtil);
        ReflectionTestUtils.setField(securityConfig, "userDetailsService", userDetailsService);
        ReflectionTestUtils.setField(securityConfig, "jwtAuthenticationFilter", jwtAuthenticationFilter);
    }

    @Test
    void securityFilterChain_shouldDisableCsrf() throws Exception {
        // Mock the chain of method calls on HttpSecurity
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        // Call the method under test
        securityConfig.securityFilterChain(httpSecurity);

        // Verify that csrf().disable() was called.
        verify(httpSecurity).csrf(any());
    }

    @Test
    void securityFilterChain_shouldBeStateless() throws Exception {
        // Mock the chain of method calls on HttpSecurity
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        // Call the method under test
        securityConfig.securityFilterChain(httpSecurity);

        // Verify that sessionManagement() was called.
        verify(httpSecurity).sessionManagement(any());
    }

    @Test
    void securityFilterChain_shouldConfigureAuthorization() throws Exception {
        // Mock the chain of method calls on HttpSecurity
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        // Call the method under test
        securityConfig.securityFilterChain(httpSecurity);

        // Verify that authorizeHttpRequests() was called with a customizer.
        verify(httpSecurity).authorizeHttpRequests(any());
    }

    @Test
    void securityFilterChain_shouldAddJwtFilterBeforeUsernamePasswordAuthenticationFilter() throws Exception {
        // Mock the chain of method calls on HttpSecurity
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        // Call the method under test
        securityConfig.securityFilterChain(httpSecurity);

        // Verify that addFilterBefore was called with the correct arguments
        verify(httpSecurity).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        // Call the method under test
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Assert that the returned object is an instance of BCryptPasswordEncoder
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    void authenticationManager_shouldReturnAuthenticationManagerFromConfig() throws Exception {
        // Mock the behavior of AuthenticationConfiguration
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthenticationManager);

        // Call the method under test
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(authenticationConfiguration);

        // Assert that the returned object is the one from the mock
        assertNotNull(authenticationManager);
        assertSame(mockAuthenticationManager, authenticationManager);
    }
}