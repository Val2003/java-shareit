package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("POST /users : create user from DTO - {}", userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id,
                                     @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} : update user by ID from DTO - {}", id, userDto);
        return ResponseEntity
                .status(HttpStatus.OK).body(userService.updateUser(userDto, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        log.info("GET /users/{} : get user by ID", id);
        return ResponseEntity
                .status(HttpStatus.OK).body(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("GET /users : get list of all users");
        return ResponseEntity
                .status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        log.info("DELETE /users/{} : delete user by ID", id);
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}