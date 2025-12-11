package ru.practicum.shareit.request.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.*;

@Repository
public class InMemoryItemRequestRepository implements ItemRequestRepository {
    private final Map<Long, ItemRequest> itemRequests = new HashMap<>();

    @Override
    public List<ItemRequest> findAllItemRequests() {
        return new ArrayList<>(itemRequests.values());
    }

    @Override
    public Optional<ItemRequest> findItemRequestById(Long id) {
        return Optional.ofNullable(itemRequests.get(id));
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest request) {
        Long id = request.getId();
        itemRequests.put(id, request);
        return itemRequests.get(id);
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest request) {
        Long id = request.getId();
        itemRequests.put(id, request);
        return itemRequests.get(id);
    }

    @Override
    public void deleteItemRequestById(Long id) {
        itemRequests.remove(id);
    }
}
