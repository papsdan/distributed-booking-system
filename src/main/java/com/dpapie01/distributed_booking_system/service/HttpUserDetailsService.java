package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * This is a service class implementing Spring Security's UserDetailsService to load user authentication data.
 */
@RequiredArgsConstructor
@Service
public class HttpUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    /**
     * Loads a user by email, builds authority roles with the "ROLE_" prefix and returns a Spring Security UserDetails object.
     * Throws UsernameNotFoundException if no user matching the provided email is found.
     * @param email the email address identifying the user
     * @return the populated Spring Security UserDetails instance
     */
    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " not found"));

        final List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // username
                user.getPassword(), // password
                user.getActive(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                roles // authorities
        );

    }
}
