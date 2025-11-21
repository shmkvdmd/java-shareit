package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User createUser(User user) {
        Long id = user.getId();
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
    }
}
