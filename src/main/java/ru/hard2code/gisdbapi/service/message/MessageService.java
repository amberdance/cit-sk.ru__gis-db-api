package ru.hard2code.gisdbapi.service.message;

import ru.hard2code.gisdbapi.domain.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessages();

    Message getMessageById(long id);

    Message createMessage(Message msg);

    void deleteAll();

    void deleteById(long id);

    Message updateMessage(long id, Message msg);

    Message partialUpdateMessage(long id, Message msg);

    List<Message> findMessageByChatId(String chatId);
}
