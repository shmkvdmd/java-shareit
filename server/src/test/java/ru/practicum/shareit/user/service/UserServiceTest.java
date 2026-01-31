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
}