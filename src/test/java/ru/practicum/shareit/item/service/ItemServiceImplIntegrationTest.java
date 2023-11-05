package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.EntityNotFoundException;
import ru.practicum.shareit.item.dto.AnswerItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@Transactional
@SpringBootTest
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @DirtiesContext
    @Test
    void createItem() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@ya.ru");
        User savedUser = userRepository.save(user);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);

        ItemDto savedItem = itemService.createItem(savedUser.getId(), itemDto);

        assertNotNull(itemDto);
        assertEquals(itemDto.getName(), savedItem.getName());
        assertEquals(itemDto.getDescription(), savedItem.getDescription());
        assertEquals(itemDto.getAvailable(), savedItem.getAvailable());

    }

    @DirtiesContext
    @Test
    void createItem_invalidItemRequest() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@ya.ru");
        User savedUser = userRepository.save(user);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);

        assertThrows(EntityNotFoundException.class, () -> itemService.createItem(savedUser.getId(), itemDto));
    }

    @DirtiesContext
    @Test
    void updateItem_byWrongUser() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@ya.ru");
        User savedUser = userRepository.save(user);

        Item oldItem = new Item();
        oldItem.setName("item");
        oldItem.setDescription("desc");
        oldItem.setAvailable(true);
        oldItem.setOwner(savedUser);
        itemRepository.save(oldItem);

        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("update");

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(oldItem.getId(), itemDto, 99L));
    }

    @DirtiesContext
    @Test
    void updateItem_wrongItemId() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@ya.ru");
        User savedUser = userRepository.save(user);

        Item oldItem = new Item();
        oldItem.setName("item");
        oldItem.setDescription("desc");
        oldItem.setAvailable(true);
        oldItem.setOwner(savedUser);
        itemRepository.save(oldItem);

        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("update");

        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(99L, itemDto, savedUser.getId()));
    }

    @DirtiesContext
    @Test
    void getItemsByUser() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@ya.ru");
        User savedUser = userRepository.save(user);

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("desc1");
        item1.setAvailable(true);
        item1.setOwner(savedUser);
        Item savedItem1 = itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("item2");
        item2.setDescription("desc2");
        item2.setAvailable(true);
        item2.setOwner(savedUser);
        itemRepository.save(item2);

        Comment comment = new Comment();
        comment.setAuthor(savedUser);
        comment.setText("comment");
        comment.setItem(savedItem1);
        commentRepository.save(comment);

        List<AnswerItemDto> items = itemService.getItemsByUser(user.getId(), pageable);

        assertNotNull(items);
        assertEquals(items.size(), 2);
        assertEquals(items.get(0).getName(), item1.getName());
        assertEquals(items.get(0).getComments().get(0).getText(), comment.getText());
        assertEquals(items.get(1).getName(), item2.getName());
        assertEquals(items.get(1).getComments().size(), 0);
    }

}