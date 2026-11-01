package com.ecommerce.security;

import com.ecommerce.auth.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        String emailToSearch = identifier != null ? identifier.trim() : "";
        if ("admin".equalsIgnoreCase(emailToSearch)) {
            emailToSearch = "admin@ecommerce.com";
        }
        return userRepo.findByEmail(emailToSearch)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + identifier));
    }
}
