package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    ItemDto createItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@PathVariable Long itemId,
                       @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.update(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/search")
    List<ItemDto> getItemsByText(@RequestParam(name = "text") String text) {
        return itemService.getByText(text);
    }

    @DeleteMapping("/{itemId}")
    void deleteItem(@PathVariable Long itemId) {
        itemService.delete(itemId);
    }
}
