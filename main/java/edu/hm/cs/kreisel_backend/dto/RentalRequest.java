package edu.hm.cs.kreisel_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RentalRequest {
    @NotNull
    private Long equipmentId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // Optional: Für Tests oder spezielle Fälle, im echten Betrieb meist nicht nötig
    private Long userId;
}