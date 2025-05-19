package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.model.User;
import edu.hm.cs.kreisel_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    // NEU: Profilinfos für eingeloggten User
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Nicht eingeloggt");
        }
        User user = userRepo.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User nicht gefunden");
        }
        return ResponseEntity.ok(user);
    }
}