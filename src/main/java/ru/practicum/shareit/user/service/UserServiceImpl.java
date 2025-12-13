package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.ServiceUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ServiceUtils utils;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto userDto) {
        User user = utils.getUserOrThrow(id);

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
        return userMapper.toDto(utils.getUserOrThrow(id));
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
}
