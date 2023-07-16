package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

public class UserMapper {
    public static User toUser(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getName(), userCreateDto.getEmail());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getUserId(), user.getName(), user.getEmail());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getUserId(), user.getName());
    }
}
