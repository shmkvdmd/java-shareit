package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, ItemRequestDto requestDto) {
        User requestor = getUserOrThrow(userId);

        ItemRequest request = mapper.toEntity(requestDto, requestor);
        return mapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestWithItemsDto> getOwnRequests(Long userId) {
        getUserOrThrow(userId);
        List<ItemRequest> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream().map(mapper::toDtoWithItems).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllRequests(Long userId) {
        getUserOrThrow(userId);
        List<ItemRequest> requests = requestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);
        return requests.stream().map(mapper::toDtoWithItems).collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithItemsDto getById(Long userId, Long requestId) {
        getUserOrThrow(userId);
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionConstants.REQUEST_NOT_FOUND, requestId)));

        return mapper.toDtoWithItems(request);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }
}