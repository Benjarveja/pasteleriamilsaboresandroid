package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.UserProfileResponse;
import cl.milsabores.pasteleria.dto.UserProfileUpdateRequest;
import cl.milsabores.pasteleria.entity.User;
import cl.milsabores.pasteleria.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperService mapper;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public UserProfileResponse getCurrentProfile(String email) {
        return mapper.toProfile(getByEmail(email));
    }

    @Transactional
    public UserProfileResponse updateProfile(UUID userId, UserProfileUpdateRequest request) {
        User user = getById(userId);
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            throw new IllegalArgumentException("El correo indicado ya está asociado a otra cuenta.");
        }

        user.setEmail(request.getEmail().toLowerCase());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRun(request.getRun());
        user.setBirthDate(request.getBirthDate());
        user.setStreet(request.getStreet());
        user.setRegion(request.getRegion());
        user.setComuna(request.getComuna());
        user.setAddress(composeAddress(request));
        return mapper.toProfile(user);
    }

    public User registerUser(User user, String rawPassword) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setAddress(composeAddress(user.getStreet(), user.getComuna(), user.getRegion()));
        return userRepository.save(user);
    }

    public String generateGuestPassword() {
        return RandomStringUtils.randomAlphanumeric(40);
    }

    private String composeAddress(UserProfileUpdateRequest request) {
        return composeAddress(request.getStreet(), request.getComuna(), request.getRegion());
    }

    private String composeAddress(String street, String comuna, String region) {
        return String.join(", ",
                street == null ? "" : street,
                comuna == null ? "" : comuna,
                region == null ? "" : region).replaceAll("(^, )|(, $)", "").replaceAll(", ,", ",");
    }

    public User findByEmailOrNull(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
