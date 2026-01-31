package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{id}")
    UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping("/{id}")
    UserDto getUserById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    List<UserDto> getUsers() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    void deleteUserById(@PathVariable Long id) {
        userService.delete(id);
    }
}

