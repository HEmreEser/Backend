package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.dto.AuthRequestDto;
import edu.hm.cs.kreisel_backend.dto.AuthResponseDto;
import edu.hm.cs.kreisel_backend.model.User;
import edu.hm.cs.kreisel_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        try {
            User user = authService.authenticateUser(request.getEmail(), request.getPassword());

            // Einfache Response mit User-Daten (kein JWT)
            AuthResponseDto response = new AuthResponseDto(
                    user.getEmail(),
                    "fake-token-" + user.getId(), // Fake Token für Frontend
                    user.getRole()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                    new AuthResponseDto(null, null, null)
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequestDto request) {
        try {
            authService.registerUser(request);
            return ResponseEntity.ok("Registrierung erfolgreich");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Fake-Endpoint um aktuellen User zu simulieren
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> getCurrentUser(@RequestParam String email) {
        try {
            User user = authService.getUserByEmail(email);
            AuthResponseDto response = new AuthResponseDto(
                    user.getEmail(),
                    "fake-token-" + user.getId(),
                    user.getRole()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}