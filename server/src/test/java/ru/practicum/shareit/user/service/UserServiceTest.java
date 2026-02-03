package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_shouldSaveAndReturnDto() {
        UserDto dto = new UserDto(null, "test", "test@mail.ru");
        User entity = User.builder().id(1L).name("test").email("test@mail.ru").build();
        UserDto savedDto = new UserDto(1L, "test", "test@mail.ru");

        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(savedDto);

        UserDto result = userService.create(dto);

        assertEquals(savedDto, result);
        verify(userRepository).save(entity);
    }

    @Test
    void update_whenUserNotFound_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.update(1L, new UserDto(null, "New", "new@@mail.ru")));
    }

    @Test
    void getById_whenNotFound_shouldThrowNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getById(1L));
    }

    @Test
    void delete_shouldDeleteUser() {
        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void update_shouldUpdateOnlyName() {
        Long userId = 1L;
        User user = User.builder().id(userId).name("old").email("old@mail.ru").build();
        UserDto updateDto = new UserDto(null, "new", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userMapper.toDto(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            return new UserDto(u.getId(), u.getName(), u.getEmail());
        });

        UserDto result = userService.update(userId, updateDto);

        assertEquals("new", result.name());
        assertEquals("old@mail.ru", result.email());
    }

    @Test
    void getAll_shouldReturnList() {
        User user = User.builder().id(1L).build();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDto(1L, "n", "e"));

        List<UserDto> result = userService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}