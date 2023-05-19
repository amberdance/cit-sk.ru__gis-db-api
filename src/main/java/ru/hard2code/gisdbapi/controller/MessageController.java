package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.domain.dto.MessageDto;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.domain.mapper.MessageMapper;
import ru.hard2code.gisdbapi.service.message.MessageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Route.MESSAGES)
@Tag(name = "MessageController", description = "User questions management " +
        "API")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;


    @GetMapping
    public List<MessageDto> getAllMessages() {
        return messageService.getAllMessages()
                .stream().map(messageMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public MessageDto getMessageById(@PathVariable("id") long id) {
        return messageMapper.toDto(messageService.getMessageById(id));
    }

    @PostMapping
    public MessageDto createMessage(@RequestBody Message msg) {
        return messageMapper.toDto(messageService.createMessage(msg));
    }

    @PutMapping("{id}")
    public MessageDto updateMessage(@PathVariable("id") long id,
                                    @RequestBody Message msg) {
        return messageMapper.toDto(messageService.updateMessage(id, msg));
    }

    @PatchMapping("{id}")
    public MessageDto partialUpdateMessage(@PathVariable("id") long id,
                                        @RequestBody Message msg) {
        return messageMapper.toDto(messageService.partialUpdateMessage(id, msg));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("id") long id) {
        messageService.deleteById(id);
    }


}
