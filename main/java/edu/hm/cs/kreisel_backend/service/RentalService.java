package edu.hm.cs.kreisel_backend.service;

import edu.hm.cs.kreisel_backend.model.Item;
import edu.hm.cs.kreisel_backend.model.Rental;
import edu.hm.cs.kreisel_backend.model.User;
import edu.hm.cs.kreisel_backend.repository.ItemRepository;
import edu.hm.cs.kreisel_backend.repository.RentalRepository;
import edu.hm.cs.kreisel_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {

    private static final int MAX_ACTIVE_RENTALS = 5;
    private static final int MAX_RENTAL_DAYS = 60; // 2 Monate

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalsByUser(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    public List<Rental> getActiveRentalsByUser(Long userId) {
        return rentalRepository.findByUserIdAndReturnDateIsNull(userId);
    }

    public List<Rental> getHistoricalRentalsByUser(Long userId) {
        return rentalRepository.findByUserIdAndReturnDateIsNotNull(userId);
    }

    public Optional<Rental> getActiveRentalForItem(Long itemId) {
        return rentalRepository.findByItemIdAndReturnDateIsNull(itemId);
    }

    public Rental rentItem(Long userId, Rental rentalRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemRepository.findById(rentalRequest.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Prüfen, ob Item verfügbar ist
        if (!item.isAvailable()) {
            throw new RuntimeException("Item ist nicht verfügbar");
        }

        // Prüfen, ob User maximal 5 aktive Ausleihen hat
        List<Rental> activeRentals = getActiveRentalsByUser(userId);
        if (activeRentals.size() >= MAX_ACTIVE_RENTALS) {
            throw new RuntimeException("Maximale Anzahl aktiver Ausleihen (5) erreicht");
        }

        // Prüfen, ob Item aktuell schon ausgeliehen ist (doppelte Absicherung)
        if (getActiveRentalForItem(item.getId()).isPresent()) {
            throw new RuntimeException("Item ist bereits ausgeliehen");
        }

        // Rental initialisieren
        rentalRequest.setUser(user);
        rentalRequest.setItem(item);
        rentalRequest.setRentalDate(LocalDate.now());
        rentalRequest.setReturnDate(null);
        rentalRequest.setExtended(false);

        // Item als nicht verfügbar markieren
        item.setAvailable(false);
        itemRepository.save(item);

        return rentalRepository.save(rentalRequest);
    }

    public Rental extendRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new RuntimeException("Rental ist bereits zurückgegeben");
        }

        if (rental.isExtended()) {
            throw new RuntimeException("Verlängerung bereits genutzt");
        }

        rental.setExtended(true);
        // Rückgabedatum verlängern um z.B. 30 Tage
        rental.setReturnDate(rental.getRentalDate().plusDays(MAX_RENTAL_DAYS + 30));
        return rentalRepository.save(rental);
    }

    public Rental returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new RuntimeException("Rental ist bereits zurückgegeben");
        }

        rental.setReturnDate(LocalDate.now());

        // Item als verfügbar markieren
        Item item = rental.getItem();
        item.setAvailable(true);
        itemRepository.save(item);

        return rentalRepository.save(rental);
    }

}
