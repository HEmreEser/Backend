package edu.hm.cs.kreisel_backend.dto;

import edu.hm.cs.kreisel_backend.model.Category;
import edu.hm.cs.kreisel_backend.model.Equipment;
import edu.hm.cs.kreisel_backend.model.Location;
import lombok.Getter;

@Getter
public class EquipmentResponse {
    private final Long id;
    private final String name;
    private final String type;
    private final String description;
    private final boolean available;
    private final Long categoryId;
    private final String categoryName;
    private final Long locationId;
    private final String locationName;

    public EquipmentResponse(Equipment equipment) {
        this.id = equipment.getId();
        this.name = equipment.getName();
        this.type = equipment.getType();
        this.description = equipment.getDescription();
        this.available = equipment.isAvailable();

        Category category = equipment.getCategory();
        this.categoryId = category != null ? category.getId() : null;
        this.categoryName = category != null ? category.getName() : null;

        Location location = equipment.getLocation();
        this.locationId = location != null ? location.getId() : null;
        this.locationName = location != null ? location.getName() : null;
    }
}