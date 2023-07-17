package ru.practicum.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.pagination.PaginationMapper;
import ru.practicum.user.dto.UserCreateDto;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@AllArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(name = "ids", required = false) List<Integer> userIds,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                     @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET, /admin/users, userIds = {}", userIds);
        List<UserDto> userDtos = userService.getAllUsers(userIds, PaginationMapper.toPageable(from, size));
        log.info("users received = {}", userDtos);
        return userDtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("POST, /admin/users, userCreateDto = {}", userCreateDto);
        UserDto user = userService.createUser(userCreateDto);
        log.info("User create {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId) {
        log.info("DELETE, /admin/users/{userId}, userId = {}", userId);
        userService.removeUser(userId);
        log.info("deleting a user = {} successfully", userId);
    }
}
