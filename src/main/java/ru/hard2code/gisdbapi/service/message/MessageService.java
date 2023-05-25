package ru.hard2code.gisdbapi.service.message;

import ru.hard2code.gisdbapi.domain.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> findAllMessages();

    Message findMessageById(long id);

    List<Message> findMessageByChatId(String chatId);

    Message createMessage(Message msg);

    Message updateMessage(long id, Message msg);

    Message partialUpdateMessage(long id, Message msg);

    void deleteMessageById(long id);


}
