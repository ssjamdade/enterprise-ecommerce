package com.ecommerce.security.config;

import com.ecommerce.auth.entity.RoleEntity;
import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.auth.repository.RoleRepo;
import com.ecommerce.auth.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@ecommerce.com";

        // Step 1: If admin already exists in the database, do nothing
        if (userRepo.existsByEmail(adminEmail)) {
            return;
        }

        // Step 2: Get the ROLE_ADMIN role (creates it if missing)
        RoleEntity adminRole = roleRepo.findByName(RoleEntity.Role.ROLE_ADMIN).orElse(null);
        if (adminRole == null) {
            adminRole = roleRepo.save(RoleEntity.builder()
                    .name(RoleEntity.Role.ROLE_ADMIN)
                    .description("System Administrator")
                    .build());
        }

        // Step 3: Create the admin user with password "admin@12345"
        UserEntity admin = UserEntity.builder()
                .firstName("Admin")
                .lastName("User")
                .email(adminEmail)
                .phone("0000000000")
                .password(passwordEncoder.encode("admin@12345"))
                .roles(Set.of(adminRole))
                .enabled(true)
                .emailVerified(true)
                .build();

        // Step 4: Save to database
        userRepo.save(admin);
        System.out.println(">>> Default Admin created: " + adminEmail + " with password admin@12345");
    }
}
