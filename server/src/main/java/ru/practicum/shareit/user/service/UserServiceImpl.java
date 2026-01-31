package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User user = getUserOrThrow(id);

        if (userDto.name() != null && !userDto.name().isBlank()) {
            user.setName(userDto.name());
        }
        if (userDto.email() != null && !userDto.email().isBlank()) {
            user.setEmail(userDto.email());
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Long id) {
        return userMapper.toDto(getUserOrThrow(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }
}
