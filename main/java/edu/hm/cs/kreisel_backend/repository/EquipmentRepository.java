package edu.hm.cs.kreisel_backend.repository;

import edu.hm.cs.kreisel_backend.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByAvailableTrue();
    List<Equipment> findByAvailable(boolean available);

    // Original methods
    List<Equipment> findByCategory_NameAndAvailableTrue(String category);
    List<Equipment> findByLocation_NameAndAvailableTrue(String location);

    // New methods by ID
    List<Equipment> findByCategoryIdAndAvailableTrue(Long categoryId);
    List<Equipment> findByLocationIdAndAvailableTrue(Long locationId);
    List<Equipment> findByCategoryIdAndLocationIdAndAvailableTrue(Long categoryId, Long locationId);

    // NEU für flexibles Filtern
    List<Equipment> findByCategoryId(Long categoryId);
    List<Equipment> findByLocationId(Long locationId);
    List<Equipment> findByCategoryIdAndAvailable(Long categoryId, boolean available);
    List<Equipment> findByLocationIdAndAvailable(Long locationId, boolean available);
    List<Equipment> findByCategoryIdAndLocationIdAndAvailable(Long categoryId, Long locationId, boolean available);
}