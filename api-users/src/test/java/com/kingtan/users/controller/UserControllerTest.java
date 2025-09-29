package com.kingtan.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingtan.users.dto.SignupRequest;
import com.kingtan.users.dto.UserDTO;
import com.kingtan.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private String version = "v1";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserDTO userDTO;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        // Configure Validator for @Valid annotations
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setValidator(validator)
                .build();

        userDTO = new UserDTO(1L,"testuser","test@example.com", Set.of("ROLE_USER"));
        signupRequest = new SignupRequest("testuser", "test@example.com", "plainPassword");
    }

    @Test
    void register_Success() throws Exception {
        when(userService.registerUser(any(SignupRequest.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/%s/users/register".formatted(version))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"plainPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(userService).registerUser(any(SignupRequest.class));
    }

    @Test
    void register_InvalidInput_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/%s/users/register".formatted(version))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"email\":\"invalid\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).registerUser(any());
    }

    @Test
    void getUser_Success() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(userDTO);

        mockMvc.perform(get("/api/%s/users/testuser".formatted(version)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(userService).findByUsername("testuser");
    }

    @Test
    void getAllUsers_Success() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/%s/users".formatted(version)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].roles[0]").value("ROLE_USER"));

        verify(userService).findAllUsers();
    }

    @Test
    void getAllUsers_EmptyList() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/%s/users".formatted(version)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).findAllUsers();
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/%s/users/1".formatted(version))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"roles\":[\"ROLE_USER\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(userService).updateUser(eq(1L), any(UserDTO.class));
    }

//    @Test
//    void updateUser_InvalidInput_ReturnsBadRequest() throws Exception {
//        mockMvc.perform(put("/api/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"\",\"email\":\"invalid\",\"roles\":[]}"))
//                .andExpect(status().isBadRequest());
//
//        verify(userService, never()).updateUser(anyLong(), any());
//    }

    @Test
    void updateUser_InvalidInput_ReturnsBadRequest() throws Exception {
        UserDTO invalidUserDTO = new UserDTO(2L, "","invalid",Collections.emptySet());

        mockMvc.perform(put("/api/%s/users/1".formatted(version))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(anyLong(), any(UserDTO.class));
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/%s/users/1".formatted(version)))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}