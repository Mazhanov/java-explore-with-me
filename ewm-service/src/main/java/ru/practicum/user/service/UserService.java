package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Integer> userIds, Pageable pageable);

    UserDto createUser(UserCreateDto dto);

    void removeUser(int userId);
}
