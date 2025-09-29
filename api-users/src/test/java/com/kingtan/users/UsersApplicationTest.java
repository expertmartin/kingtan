package com.kingtan.users;

import com.kingtan.users.repository.PasswordResetTokenRepository;
import com.kingtan.users.repository.RoleRepository;
import com.kingtan.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UsersApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    void contextLoads() {
        // Verifies that the Spring application context loads successfully
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void userRepositoryBeanExists() {
        // Verifies that UserRepository bean is created
        assertNotNull(userRepository, "UserRepository bean should be created");
        assertTrue(applicationContext.containsBean("userRepository"), "UserRepository bean should exist in context");
    }

    @Test
    void roleRepositoryBeanExists() {
        // Verifies that RoleRepository bean is created
        assertNotNull(roleRepository, "RoleRepository bean should be created");
        assertTrue(applicationContext.containsBean("roleRepository"), "RoleRepository bean should exist in context");
    }

    @Test
    void passwordResetTokenRepositoryBeanExists() {
        // Verifies that PasswordResetTokenRepository bean is created
        assertNotNull(passwordResetTokenRepository, "PasswordResetTokenRepository bean should be created");
        assertTrue(applicationContext.containsBean("passwordResetTokenRepository"), "PasswordResetTokenRepository bean should exist in context");
    }
}