package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.dto.EquipmentRequest;
import edu.hm.cs.kreisel_backend.dto.EquipmentResponse;
import edu.hm.cs.kreisel_backend.model.Category;
import edu.hm.cs.kreisel_backend.model.Equipment;
import edu.hm.cs.kreisel_backend.model.Location;
import edu.hm.cs.kreisel_backend.repository.CategoryRepository;
import edu.hm.cs.kreisel_backend.repository.EquipmentRepository;
import edu.hm.cs.kreisel_backend.repository.LocationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentRepository equipmentRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private LocationRepository locationRepo;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EquipmentRequest request) {
        try {
            Category category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category with ID " + request.getCategoryId() + " not found"));
            Location location = locationRepo.findById(request.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location with ID " + request.getLocationId() + " not found"));

            Equipment equipment = new Equipment();
            equipment.setName(request.getName());
            equipment.setType(request.getType() != null ? request.getType() : "");
            equipment.setDescription(request.getDescription() != null ? request.getDescription() : "");
            equipment.setAvailable(true);
            equipment.setCategory(category);
            equipment.setLocation(location);

            Equipment savedEquipment = equipmentRepo.save(equipment);
            return ResponseEntity.ok(new EquipmentResponse(savedEquipment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating equipment: " + e.getMessage());
        }
    }

    // Alle Equipments (egal ob verfügbar)
    @GetMapping("/all")
    public List<EquipmentResponse> getAllEquipments() {
        return equipmentRepo.findAll().stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
    }

    // Alle verfügbaren Equipments
    @GetMapping
    public List<EquipmentResponse> getAvailable() {
        return equipmentRepo.findByAvailableTrue().stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
    }

    // Nur verfügbare einer Kategorie
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> filterByCategory(@PathVariable Long categoryId) {
        if (!categoryRepo.existsById(categoryId)) {
            return ResponseEntity.badRequest().body("Category not found");
        }
        List<EquipmentResponse> result = equipmentRepo.findByCategoryIdAndAvailableTrue(categoryId).stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // Nur verfügbare an einem Standort
    @GetMapping("/location/{locationId}")
    public ResponseEntity<?> filterByLocation(@PathVariable Long locationId) {
        if (!locationRepo.existsById(locationId)) {
            return ResponseEntity.badRequest().body("Location not found");
        }
        List<EquipmentResponse> result = equipmentRepo.findByLocationIdAndAvailableTrue(locationId).stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // Flexibles Filtern inkl. available
    @GetMapping("/filter")
    public List<EquipmentResponse> filterByCategoryLocationAndAvailability(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) Boolean available
    ) {
        List<Equipment> result;
        if (categoryId != null && locationId != null && available != null) {
            result = equipmentRepo.findByCategoryIdAndLocationIdAndAvailable(categoryId, locationId, available);
        } else if (categoryId != null && available != null) {
            result = equipmentRepo.findByCategoryIdAndAvailable(categoryId, available);
        } else if (locationId != null && available != null) {
            result = equipmentRepo.findByLocationIdAndAvailable(locationId, available);
        } else if (categoryId != null) {
            result = equipmentRepo.findByCategoryId(categoryId);
        } else if (locationId != null) {
            result = equipmentRepo.findByLocationId(locationId);
        } else if (available != null) {
            result = equipmentRepo.findByAvailable(available);
        } else {
            result = equipmentRepo.findAll();
        }
        return result.stream()
                .map(EquipmentResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @GetMapping("/locations")
    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }
}