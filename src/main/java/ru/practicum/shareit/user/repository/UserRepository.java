package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(Long id);
}
