package com.kingtan.users.service;

import com.kingtan.users.dto.SignupRequest;
import com.kingtan.users.dto.UserDTO;
import com.kingtan.users.model.Role;
import com.kingtan.users.model.User;
import com.kingtan.users.repository.RoleRepository;
import com.kingtan.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role userRole;
    private SignupRequest signupRequest;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        userRole = new Role();
        userRole.setName("ROLE_USER");
        user.setRoles(new HashSet<>(Set.of(userRole)));

        signupRequest = new SignupRequest("testuser","test@example.com","plainPassword");

        userDTO = new UserDTO(1L,"testuser","test@example.com",Set.of("ROLE_USER"));
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.registerUser(signupRequest);

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("test@example.com", result.email());
        assertEquals(Set.of("ROLE_USER"), result.roles());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_UsernameTaken_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(signupRequest));
        assertEquals("Username is already taken", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_EmailInUse_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(signupRequest));
        assertEquals("Email is already in use", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_RoleNotFound_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(signupRequest));
        assertEquals("Role not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDTO result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("test@example.com", result.email());
        assertEquals(Set.of("ROLE_USER"), result.roles());
    }

    @Test
    void findByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findByUsername("testuser"));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.findAllUsers();

        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).username());
        assertEquals("test@example.com", result.get(0).email());
        assertEquals(Set.of("ROLE_USER"), result.get(0).roles());
    }

    @Test
    void findAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDTO> result = userService.findAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertNotNull(result);
        assertEquals("testuser", result.username());
        assertEquals("test@example.com", result.email());
        assertEquals(Set.of("ROLE_USER"), result.roles());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, userDTO));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_RoleNotFound_ThrowsException() {
        UserDTO invalidDto = new UserDTO(userDTO.id(), userDTO.username(), userDTO.email(), Set.of("ROLE_INVALID"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_INVALID")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(1L, invalidDto));
        assertEquals("Role not found: ROLE_INVALID", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFound_ThrowsException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }
}