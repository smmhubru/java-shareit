package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerIdOrderById(Long ownerId, Pageable pageable);

    List<Item> findByNameOrDescriptionContainingAllIgnoreCase(String name, String description, Pageable pageable);
}
