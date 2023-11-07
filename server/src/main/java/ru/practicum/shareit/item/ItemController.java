package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exceptions.EntityNotAvailable;
import ru.practicum.shareit.item.dto.AnswerItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    public static final String userIdHeader = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody @Valid ItemDto itemDto,
                                              @RequestHeader(userIdHeader) Long userId) {
        log.info("POST /items : user ID {} creates item from DTO - {}", userId, itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("itemId") Long itemId,
                                              @RequestBody ItemDto itemDto,
                                              @RequestHeader(userIdHeader) Long userId) {
        log.info("PATCH /items/{} : update item by ID from user ID {}, item DTO - {}", itemId, userId, itemDto);
        return ResponseEntity.status(HttpStatus.OK).body(itemService.updateItem(itemId, itemDto, userId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<AnswerItemDto> getItem(@PathVariable("itemId") Long itemId,
                                                 @RequestHeader(userIdHeader) Long userId) {
        log.info("GET /items/{} : get item by ID from user ID {}", itemId, userId);
        return ResponseEntity
                .status(HttpStatus.OK).body(itemService.getItem(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<AnswerItemDto>> getItemsByUser(
            @RequestHeader(userIdHeader) Long userId,
            @RequestParam(value = "from", defaultValue = "0", required = false) int from,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (from < 0 || size < 1) {
            throw new EntityNotAvailable("Invalid \"size\" or \"from\"");
        }
        log.info("GET /items?from={}&size={} : get list of items from user ID {}", from, size, userId);
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemsByUser(userId, PageRequest.of(from / size, size)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getUsersAvailableItems(
            @RequestHeader(userIdHeader) Long userId,
            @RequestParam String text,
            @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) int from,
            @Positive @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (from < 0 || size < 1) {
            throw new EntityNotAvailable("Invalid \"size\" or \"from\"");
        }
        log.info("GET /items/search?text={}&from={}&size={} : get list of available items of user ID {} with text",
                text, from, size, userId);
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getAvailableItems(userId, text, PageRequest.of(from / size, size)));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long itemId,
                                                    @Valid @RequestBody CommentDto commentDto,
                                                    @RequestHeader(userIdHeader) Long userId) {
        log.info("POST /items/{}/comment : user ID {} creates comment - {}", itemId, userId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(itemService.createComment(itemId, userId, commentDto));
    }

}