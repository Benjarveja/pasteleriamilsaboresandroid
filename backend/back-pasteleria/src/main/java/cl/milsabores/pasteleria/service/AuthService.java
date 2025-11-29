package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.AuthRequest;
import cl.milsabores.pasteleria.dto.AuthResponse;
import cl.milsabores.pasteleria.dto.RegisterRequest;
import cl.milsabores.pasteleria.dto.RefreshRequest;
import cl.milsabores.pasteleria.entity.Role;
import cl.milsabores.pasteleria.entity.User;
import cl.milsabores.pasteleria.repository.UserRepository;
import cl.milsabores.pasteleria.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return buildTokens(user);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("Ya existe una cuenta registrada con este correo.");
        }
        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRun(request.getRun());
        user.setStreet(request.getStreet());
        user.setRegion(request.getRegion());
        user.setComuna(request.getComuna());
        user.setBirthDate(request.getBirthDate());
        user.setRoles(List.of(Role.CLIENT));
        user.setAddress(composeAddress(request.getStreet(), request.getComuna(), request.getRegion()));
        userRepository.save(user);
        return buildTokens(user);
    }

    public AuthResponse refresh(RefreshRequest request) {
        String email = jwtService.extractUsername(request.getRefreshToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return buildTokens(user);
    }

    private AuthResponse buildTokens(User user) {
        String jwt = jwtService.generateToken(Map.of(
                "userId", user.getId().toString(),
                "roles", user.getRoles().stream().map(Enum::name).toList()
        ), user.toSecurityUser());
        String refresh = jwtService.generateToken(Map.of("type", "refresh"), user.toSecurityUser());
        return AuthResponse.builder()
                .token(jwt)
                .refreshToken(refresh)
                .email(user.getEmail())
                .userId(user.getId().toString())
                .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
    }

    private String composeAddress(String street, String comuna, String region) {
        return String.join(", ",
                street == null ? "" : street,
                comuna == null ? "" : comuna,
                region == null ? "" : region).replaceAll("(^, )|(, $)", "").replaceAll(", ,", ",");
    }
}

