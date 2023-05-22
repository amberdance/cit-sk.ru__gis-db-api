package ru.hard2code.gisdbapi.service.message;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.domain.mapper.MessageMapper;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.MessageRepository;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageMapper messageMapper;


    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(long id) {
        return messageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Message.class, id));
    }

    @Override
    @Transactional
    public Message createMessage(Message msg) {
        msg.setId(null);
        var user = msg.getUser();

        if (user.getId() != null && user.getId() != 0) {
            msg.setUser(userService.findUserById(user.getId()));
        }

        return messageRepository.save(msg);
    }

    @Override
    public void deleteAll() {
        messageRepository.deleteAllInBatch();
    }

    @Override
    public void deleteById(long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public Message updateMessage(long id, Message msg) {
        var message = getMessageById(id)
                .toBuilder()
                .question(msg.getQuestion())
                .answer(msg.getAnswer())
                .build();

        return messageRepository.save(message);
    }

    @Override
    public Message partialUpdateMessage(long id, Message msg) {
        var updatedMessage =
                messageMapper.partialUpdate(messageMapper.toDto(msg),
                        getMessageById(id));
        return messageRepository.save(updatedMessage);
    }

    @Override
    public List<Message> findMessageByChatId(String chatId) {
        return messageRepository.findByUser_ChatId(chatId);
    }

}
