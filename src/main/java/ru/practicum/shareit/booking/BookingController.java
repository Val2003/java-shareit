package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.booking.dto.AnswerBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity <AnswerBookingDto> createBooking(
            @RequestHeader(userIdHeader) Long userId,
            @Valid @RequestBody BookingDto bookingDto) {
        log.info("POST /bookings : create booking from DTO - {}", bookingDto);
        return ResponseEntity  .status(HttpStatus.CREATED)
                .body( bookingService.createBooking(userId, bookingDto));

    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity <AnswerBookingDto> confirmationBooking(
            @RequestHeader(userIdHeader) Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        log.info("PATCH /bookings/{} : update booking's status by ID - approved {}, user ID {}",
                bookingId, approved, userId);
        return ResponseEntity   .status(HttpStatus.OK)
                .body(bookingService.confirmationBooking(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity <AnswerBookingDto> getBooking(@RequestHeader(userIdHeader) Long userId,
                                                        @PathVariable Long bookingId) {
        log.info("GET /bookings/{} : get booking by ID by user ID {}", bookingId, userId);
        return ResponseEntity   .status(HttpStatus.OK)
                .body(bookingService.getBooking(userId, bookingId));
    }

    @GetMapping
    public ResponseEntity <List<AnswerBookingDto>> getAllBookingByUser(
            @RequestHeader(userIdHeader) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("GET /bookings?state={} : get list of bookings by user ID {} with state", state, userId);
        return ResponseEntity   .status(HttpStatus.OK)
                .body(bookingService.getAllBookingByUser(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity <List<AnswerBookingDto>> getAllBookingByOwner(
            @RequestHeader(userIdHeader) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("GET /bookings/owner?state={} : get list of bookings by owner ID {} with state", state, userId);
        return ResponseEntity   .status(HttpStatus.OK)
                .body(bookingService.getAllBookingByOwner(userId, state));
    }

}