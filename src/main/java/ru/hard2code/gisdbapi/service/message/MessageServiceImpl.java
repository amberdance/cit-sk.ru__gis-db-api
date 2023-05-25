package ru.hard2code.gisdbapi.service.message;

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
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message findMessageById(long id) {
        return messageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Message.class, id));
    }

    @Override
    public List<Message> findMessageByChatId(String chatId) {
        return messageRepository.findByUser_ChatId(chatId);
    }

    @Override
    public Message createMessage(Message msg) {
        msg.setId(null);
        var user = msg.getUser();

        if (user.getId() != null && user.getId() != 0) {
            msg.setUser(userService.findUserById(user.getId()));
        }

        return messageRepository.save(msg);
    }

    @Override
    public Message updateMessage(long id, Message msg) {
        var optional = messageRepository.findById(id);

        if (optional.isEmpty()) {
            return createMessage(msg);
        }

        var message = optional.get()
                .toBuilder()
                .question(msg.getQuestion())
                .answer(msg.getAnswer())
                .user(msg.getUser())
                .build();

        return messageRepository.save(message);
    }

    @Override
    public Message partialUpdateMessage(long id, Message msg) {
        var updatedMessage =
                messageMapper.partialUpdate(messageMapper.toDto(msg),
                        findMessageById(id));
        return messageRepository.save(updatedMessage);
    }

    @Override
    public void deleteMessageById(long id) {
        messageRepository.deleteById(id);
    }


}
