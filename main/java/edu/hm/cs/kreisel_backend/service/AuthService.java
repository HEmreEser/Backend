package edu.hm.cs.kreisel_backend.service;

import edu.hm.cs.kreisel_backend.dto.AuthRequestDto;
import edu.hm.cs.kreisel_backend.model.User;
import edu.hm.cs.kreisel_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private static final Pattern HM_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@hm\\.edu$");

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Benutzer nicht gefunden");
        }

        // Einfacher String-Vergleich für Fake Auth
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Ungültiges Passwort");
        }

        return user;
    }

    public User registerUser(AuthRequestDto registerRequest) {
        if (!isValidHmEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Nur HM-E-Mail-Adressen sind erlaubt");
        }

        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 6) {
            throw new IllegalArgumentException("Passwort muss mindestens 6 Zeichen lang sein");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
            throw new IllegalArgumentException("Benutzer mit dieser E-Mail existiert bereits");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword()); // Klartext für Fake Auth

        // Admin-Rolle wenn Email mit "admin" beginnt
        if (registerRequest.getEmail().startsWith("admin")) {
            user.setRole(User.Role.ADMIN);
        } else {
            user.setRole(User.Role.USER);
        }

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Benutzer nicht gefunden");
        }
        return user;
    }

    private boolean isValidHmEmail(String email) {
        return email != null && HM_EMAIL_PATTERN.matcher(email).matches();
    }
}