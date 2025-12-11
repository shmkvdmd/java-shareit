package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository {
    List<ItemRequest> findAllItemRequests();

    Optional<ItemRequest> findItemRequestById(Long id);

    ItemRequest createItemRequest(ItemRequest request);

    ItemRequest updateItemRequest(ItemRequest request);

    void deleteItemRequestById(Long id);
}
