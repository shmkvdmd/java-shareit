package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.*;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> findAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Optional<Item> findItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findItemsByText(String text) {
        String lower = text.toLowerCase();

        return items.values().stream()
                .filter(Item::getIsAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(lower) ||
                                item.getDescription().toLowerCase().contains(lower)
                )
                .toList();
    }

    @Override
    public Item createItem(Item item) {
        Long id = item.getId();
        items.put(id, item);
        return items.get(id);
    }

    @Override
    public Item updateItem(Item item) {
        Long id = item.getId();
        items.put(id, item);
        return items.get(id);
    }

    @Override
    public void deleteItemById(Long id) {
        items.remove(id);
    }
}
