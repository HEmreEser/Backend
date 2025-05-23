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
    private static final int MAX_RENTAL_DAYS = 60; // 2 Monate maximal
    private static final int EXTENSION_DAYS = 30; // Verlängerung um 30 Tage

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

        // Item-ID aus der Anfrage holen
        Long itemId = rentalRequest.getItem() != null ? rentalRequest.getItem().getId() : null;
        if (itemId == null) {
            throw new RuntimeException("Item ID ist erforderlich");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Validierungen
        validateRentalRequest(userId, item, rentalRequest);

        // Neues Rental-Objekt erstellen
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setItem(item);
        rental.setRentalDate(LocalDate.now());

        // Enddatum validieren und setzen
        LocalDate endDate = validateAndSetEndDate(rentalRequest.getEndDate());
        rental.setEndDate(endDate);

        rental.setReturnDate(null); // Noch nicht zurückgegeben
        rental.setExtended(false);

        // Item als nicht verfügbar markieren
        item.setAvailable(false);
        itemRepository.save(item);

        return rentalRepository.save(rental);
    }

    private void validateRentalRequest(Long userId, Item item, Rental rentalRequest) {
        // Prüfen, ob Item verfügbar ist
        if (!item.isAvailable()) {
            throw new RuntimeException("Item ist nicht verfügbar");
        }

        // Prüfen, ob User maximal 5 aktive Ausleihen hat
        List<Rental> activeRentals = getActiveRentalsByUser(userId);
        if (activeRentals.size() >= MAX_ACTIVE_RENTALS) {
            throw new RuntimeException("Maximale Anzahl aktiver Ausleihen (5) erreicht");
        }

        // Prüfen, ob Item aktuell schon ausgeliehen ist
        if (getActiveRentalForItem(item.getId()).isPresent()) {
            throw new RuntimeException("Item ist bereits ausgeliehen");
        }

        // Enddatum muss angegeben werden
        if (rentalRequest.getEndDate() == null) {
            throw new RuntimeException("Enddatum ist erforderlich");
        }
    }

    private LocalDate validateAndSetEndDate(LocalDate requestedEndDate) {
        LocalDate today = LocalDate.now();
        LocalDate maxEndDate = today.plusDays(MAX_RENTAL_DAYS);

        // Enddatum darf nicht in der Vergangenheit liegen
        if (requestedEndDate.isBefore(today)) {
            throw new RuntimeException("Enddatum darf nicht in der Vergangenheit liegen");
        }

        // Enddatum darf maximal 2 Monate in der Zukunft liegen
        if (requestedEndDate.isAfter(maxEndDate)) {
            throw new RuntimeException("Enddatum darf maximal " + MAX_RENTAL_DAYS + " Tage in der Zukunft liegen");
        }

        // Mindestens 1 Tag Ausleihdauer
        if (requestedEndDate.isEqual(today)) {
            throw new RuntimeException("Ausleihdauer muss mindestens 1 Tag betragen");
        }

        return requestedEndDate;
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

        // Neues Enddatum berechnen (um 30 Tage verlängern)
        LocalDate newEndDate = rental.getEndDate().plusDays(EXTENSION_DAYS);
        LocalDate maxAllowedDate = rental.getRentalDate().plusDays(MAX_RENTAL_DAYS + EXTENSION_DAYS);

        // Prüfen ob die Verlängerung das absolute Maximum überschreitet
        if (newEndDate.isAfter(maxAllowedDate)) {
            throw new RuntimeException("Verlängerung würde die maximale Ausleihdauer überschreiten");
        }

        rental.setEndDate(newEndDate);
        rental.setExtended(true);

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

    // Zusätzliche Methode: Überfällige Rentals finden
    public List<Rental> getOverdueRentals() {
        return rentalRepository.findAll().stream()
                .filter(rental -> rental.getReturnDate() == null) // Noch nicht zurückgegeben
                .filter(rental -> rental.getEndDate().isBefore(LocalDate.now())) // Überfällig
                .toList();
    }
}