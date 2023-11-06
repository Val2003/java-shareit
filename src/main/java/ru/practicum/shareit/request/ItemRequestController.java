package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.EntityNotAvailable;
import ru.practicum.shareit.request.dto.AnswerItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    public static final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<AnswerItemRequestDto> createItemRequest(@RequestHeader(userIdHeader) Long userId,
                                                                  @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST /requests : user ID {} creates itemRequest from DTO - {}", userId, itemRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemRequestService.createItemRequest(userId, itemRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<AnswerItemRequestDto>> getUsersItemRequests(@RequestHeader(userIdHeader) Long userId) {
        log.info("GET /requests : get list of itemRequests by user ID {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestService.getUsersItemRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnswerItemRequestDto>> getItemRequests(
            @RequestHeader(userIdHeader) Long userId,
            @RequestParam(value = "from", defaultValue = "0", required = false) int from,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        if (from < 0 || size < 1) {
            throw new EntityNotAvailable("Invalid \"size\" or \"from\"");
        }
        log.info("GET /requests/all?from={}&size={} : get list of itemRequests, user ID {}", from, size, userId);
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestService.getItemRequests(userId, PageRequest.of(from / size, size)));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<AnswerItemRequestDto> getItemRequest(@PathVariable("requestId") Long requestId,
                                                               @RequestHeader(userIdHeader) Long userId) {
        log.info("GET /requests/{} : get itemRequest by ID, user ID {}", requestId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestService.getItemRequestById(requestId, userId));
    }
}
