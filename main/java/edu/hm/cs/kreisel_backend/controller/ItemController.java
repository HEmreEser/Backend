package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.model.Item;
import edu.hm.cs.kreisel_backend.model.Item.*;
import edu.hm.cs.kreisel_backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // Haupt-GET-Endpunkt mit allen Filtern
    @GetMapping
    public List<Item> getFilteredItems(
            @RequestParam Location location,                    // Pflicht: Standort
            @RequestParam(required = false) Boolean available,   // Optional: Verfügbarkeit
            @RequestParam(required = false) String searchQuery,  // Optional: Textsuche
            @RequestParam(required = false) Gender gender,       // Optional: Gender
            @RequestParam(required = false) Category category,   // Optional: Kategorie
            @RequestParam(required = false) Subcategory subcategory, // Optional: Unterkategorie
            @RequestParam(required = false) String size          // Optional: Größe
    ) {
        return itemService.filterItems(
                location,
                available,
                searchQuery,
                gender,
                category,
                subcategory,
                size
        );
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item) {
        return itemService.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
    }
}
