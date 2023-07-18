package ru.practicum.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getName(), userCreateDto.getEmail());
    }

    public UserDto toUserDto(User user) {
        return new UserDto(user.getUserId(), user.getName(), user.getEmail());
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getUserId(), user.getName());
    }
}
