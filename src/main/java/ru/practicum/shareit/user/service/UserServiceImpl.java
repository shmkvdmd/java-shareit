package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.ExceptionConstants;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        Long id = getNextId();
        isValidEmail(id, userDto.email());
        User user = userRepository.createUser(userMapper.toEntity(id, userDto));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = findUserByIdOrThrow(id);

        if (userDto.name() != null) {
            user.setName(userDto.name());
        }

        if (userDto.email() != null) {
            isValidEmailFormat(userDto.email(), id);
            isValidEmail(id, userDto.email());
            user.setEmail(userDto.email());
        }

        User updated = userRepository.updateUser(user);
        return userMapper.toDto(updated);
    }


    @Override
    public UserDto getById(Long id) {
        User user = findUserByIdOrThrow(id);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAllUsers();
        return users.stream().map(userMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteUserById(id);
    }

    private long getNextId() {
        return userRepository.findAllUsers().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;
    }

    private void isValidEmail(Long userId, String email) {
        boolean exists = userRepository.findAllUsers().stream()
                .anyMatch(u -> u.getId() != null && !u.getId().equals(userId)
                        && u.getEmail() != null && u.getEmail().equalsIgnoreCase(email));
        if (exists) {
            log.warn("email already exists, userId={}, email={}", userId, email);
            throw new EmailAlreadyExistsException(String.format(ExceptionConstants.EMAIL_EXISTS, email));
        }
    }

    private void isValidEmailFormat(String email, Long userId) {
        if (email == null || email.isBlank() || !email.matches("^\\S+@\\S+\\.\\S+$")) {
            log.warn("invalid email format for userId={}, email={}", userId, email);
            throw new IllegalArgumentException(
                    String.format(ExceptionConstants.WRONG_EMAIL_FORMAT, email));
        }
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", userId);
                    return new NotFoundException(String.format(ExceptionConstants.USER_NOT_FOUND, userId));
                });
    }
}
