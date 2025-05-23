package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.model.Rental;
import edu.hm.cs.kreisel_backend.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rental>> getRentalsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.getRentalsByUser(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Rental>> getActiveRentalsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.getActiveRentalsByUser(userId));
    }

    @GetMapping("/user/{userId}/history")
    public ResponseEntity<List<Rental>> getHistoricalRentalsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rentalService.getHistoricalRentalsByUser(userId));
    }

    @PostMapping("/user/{userId}/rent")
    public ResponseEntity<Rental> rentItem(@PathVariable Long userId, @RequestBody Rental rentalRequest) {
        return ResponseEntity.ok(rentalService.rentItem(userId, rentalRequest));
    }

    @PostMapping("/{rentalId}/extend")
    public ResponseEntity<Rental> extendRental(@PathVariable Long rentalId) {
        return ResponseEntity.ok(rentalService.extendRental(rentalId));
    }

    @PostMapping("/{rentalId}/return")
    public ResponseEntity<Rental> returnRental(@PathVariable Long rentalId) {
        return ResponseEntity.ok(rentalService.returnRental(rentalId));
    }
}
