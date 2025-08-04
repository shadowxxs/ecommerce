package com.example.ecommerce.service;


import com.example.ecommerce.dto.auth.SignupRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.InvalidPasswordException;
import com.example.ecommerce.exception.UserAlreadyExistException;
import com.example.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private void validatePassword(String rawPassword) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

        if (!rawPassword.matches(regex)) {
            throw new InvalidPasswordException(
                    "Password harus minimal 6 karakter, mengandung huruf besar, huruf kecil, dan angka"
            );
        }
    }

    public User registerUser(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() || userRepository.findByEmail(request.getEmail()) .isPresent()) {
            throw new UserAlreadyExistException("Username / Email sudah dipakai");
        }

        validatePassword(request.getPassword());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        return userRepository.save(newUser);
    }
}
