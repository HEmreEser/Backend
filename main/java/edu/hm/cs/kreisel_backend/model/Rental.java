package edu.hm.cs.kreisel_backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "app_rental")
public class Rental {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private User user;

        @ManyToOne
        private Item item;

        private LocalDate rentalDate; // Wann ausgeliehen
        private LocalDate returnDate; // Wann zurückgegeben (null = aktiv)
        private boolean extended = false; // Max. 1 Verlängerung erlaubt


    }