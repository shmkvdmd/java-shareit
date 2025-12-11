package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> findAllItems();

    Optional<Item> findItemById(Long id);

    List<Item> findItemsByText(String text);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItemById(Long id);
}
