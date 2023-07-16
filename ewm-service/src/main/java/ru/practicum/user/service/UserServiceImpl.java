package ru.practicum.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.core.exception.ObjectNotFoundException;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers(List<Integer> userIds, Pageable pageable) {
        boolean usersListNullOrEmpty = userIds == null || userIds.isEmpty();

        if (usersListNullOrEmpty) {
           return userRepository.findAll(pageable)
                   .stream()
                   .map(UserMapper::toUserDto)
                   .collect(Collectors.toList());
        }

        List<User> users = userRepository.findUsersByUserIdIn(userIds, pageable);
        return users
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        User newUser = UserMapper.toUser(userCreateDto);
        return UserMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public void removeUser(int userId) {
        User user = findUserByIdAndCheck(userId);
        userRepository.delete(user);
    }

    private User findUserByIdAndCheck(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("User", id));
    }
}
