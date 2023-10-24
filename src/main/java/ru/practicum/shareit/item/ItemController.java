package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@Slf4j
//@RestController
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader(userIdHeader) Long userId) {
        log.info("POST /items : user ID {} creates item from DTO - {}", userId, itemDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.createItem(userService.getUser(userId), itemDto));

    }

    @PatchMapping("{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader(userIdHeader) Long userId,
                                              @RequestBody ItemDto itemDto,
                                              @PathVariable @Min(1) Long itemId) {
        log.info("PATCH /items/{} : update item by ID from user ID {}, item DTO - {}", itemId, userId, itemDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.updateItem(itemId, itemDto, userId));

    }

    @GetMapping("{itemId}")
    public ResponseEntity<ItemDto> getItemByItemId(@PathVariable("itemId") Long itemId, @RequestHeader(userIdHeader) Long userId) {
        log.info("GET /items/{} : get item by ID from user ID {}", itemId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.getItem(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItemsByUser(@RequestHeader(userIdHeader) Long userId) {
        log.info("GET /items : get list of items from user ID {}", userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.getItemsByUser(userId));
    }

    @GetMapping("search")
    public ResponseEntity<List<ItemDto>> getAvailableItems(
            @RequestHeader(userIdHeader) Long userId, @RequestParam String text) {
        log.info("GET /items : get list of available items of user ID {} with text {}", userId, text);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemService.getAvailableItems(userId, text));
    }

}