package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.RegisterRequestDTO;
import com.dpapie01.distributed_booking_system.dto.UserFilterDTO;
import com.dpapie01.distributed_booking_system.dto.UserRequestDTO;
import com.dpapie01.distributed_booking_system.dto.UserResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Credit;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.mapper.UserMapper;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private static final BigDecimal SIGNUP_CREDIT_AMOUNT = BigDecimal.valueOf(100);

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ProfileRepository profileRepository;
    private final CreditRepository creditRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.PLAYER);
        user.setActive(true);
        User savedUser = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(savedUser);
        profile.setPreferredLocation(location);
        profile.setGender(dto.getGender());
        profileRepository.save(profile);

        Credit signupCredit = new Credit();
        signupCredit.setUser(savedUser);
        signupCredit.setAmount(SIGNUP_CREDIT_AMOUNT);
        signupCredit.setReason("Signup Credits");
        creditRepository.save(signupCredit);

        return userMapper.toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers(UserFilterDTO filter) {
        return userRepository.searchUsers(
                        filter.getSearchQuery() == null || filter.getSearchQuery().isBlank() ? null : filter.getSearchQuery(),
                        filter.isActiveOnly(),
                        filter.getRoles() == null || filter.getRoles().isEmpty() ? null : filter.getRoles())
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public UserResponseDTO updateUser(UserRequestDTO dto, Long userId, String currentUserEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getEmail().equals(currentUserEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot change your own role or active status");
        }

        user.setActive(dto.getActive());
        user.setRole(dto.getRole());
        user.setDeactivatedAt(dto.getActive() ? null : LocalDateTime.now());

        return userMapper.toResponseDTO(userRepository.save(user));
    }
}