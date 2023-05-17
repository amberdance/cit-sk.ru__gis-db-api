package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.model.Message;
import ru.hard2code.gisdbapi.service.message.MessageService;

import java.util.List;

@RestController
@RequestMapping(Route.MESSAGES)
@Tag(name = "MessageController", description = "User questions management " +
        "API")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("{id}")
    public Message getMessageById(@PathVariable("id") long id) {
        return messageService.getMessageById(id);
    }

    @PostMapping
    public Message createMessage(@RequestBody Message msg) {
        return messageService.createMessage(msg);
    }

    @PutMapping("{id}")
    public Message updateMessage(@PathVariable("id") long id,
                                 @RequestBody Message msg) {
        return messageService.updateMessage(id, msg);
    }

    @PatchMapping("{id}")
    public Message partialUpdateMessage(@PathVariable("id") long id,
                                        @RequestBody Message msg) {
        return messageService.updateMessage(id, msg);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("id") long id) {
        messageService.deleteById(id);
    }


}
