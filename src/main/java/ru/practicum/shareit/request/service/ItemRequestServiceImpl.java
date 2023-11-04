package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.mapper.EntityMapper;
import ru.practicum.shareit.request.dto.AnswerItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public AnswerItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " does not exist"));
        ItemRequest itemRequest = EntityMapper.INSTANCE.toItemRequest(itemRequestDto, user);
        return EntityMapper.INSTANCE.toAnswerItemRequestDto(itemRequestRepository.save(itemRequest), new ArrayList<>());
    }

    @Override
    public List<AnswerItemRequestDto> getUsersItemRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " does not exist");
        }
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
        return requests.stream()
                .map(req -> EntityMapper.INSTANCE.toAnswerItemRequestDto(req, itemRepository.findAllByRequest_IdOrderByIdDesc(req.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<AnswerItemRequestDto> getItemRequests(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " does not exist");
        }
        List<ItemRequest> requests = itemRequestRepository.findRequestsWithoutOwner(userId, pageable);
        return requests.stream()
                .map(request -> EntityMapper.INSTANCE.toAnswerItemRequestDto(request,
                        itemRepository.findAllByRequest_IdOrderByIdDesc(request.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public AnswerItemRequestDto getItemRequestById(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User with ID " + userId + " does not exist");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("ItemRequest with ID " + requestId + " does not exist"));
        return EntityMapper.INSTANCE.toAnswerItemRequestDto(itemRequest, itemRepository.findAllByRequest_IdOrderByIdDesc(itemRequest.getId()));
    }

}
