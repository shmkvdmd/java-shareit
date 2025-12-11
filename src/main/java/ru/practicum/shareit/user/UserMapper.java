package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toEntity(Long id, UserDto userDto) {
        return new User(id, userDto.name(), userDto.email());
    }
}
