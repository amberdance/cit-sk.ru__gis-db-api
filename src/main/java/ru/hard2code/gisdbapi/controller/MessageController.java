package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.service.message.MessageService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.Route.MESSAGES)
@Tag(name = "MessageController", description = "User questions management " +
        "API")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.findAllMessages();
    }

    @GetMapping("{id}")
    public Message getMessageById(@PathVariable("id") long id) {
        return messageService.findMessageById(id);
    }

    @GetMapping("user/{chatId}")
    public List<Message> getMessagesByChatId(
            @PathVariable("chatId") String chatId) {
        return messageService.findMessageByChatId(chatId);

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
        return messageService.partialUpdateMessage(id, msg);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("id") long id) {
        messageService.deleteMessageById(id);
    }


}
