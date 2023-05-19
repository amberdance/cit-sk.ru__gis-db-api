package ru.hard2code.gisdbapi.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.MessageRepository;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;


    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Message getMessageById(long id) {
        return messageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Message.class, id));
    }

    @Override
    public Message createMessage(Message msg) {
        var user = msg.getUser();

        if (user.getId() != null) {
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
        var message = getMessageById(id);
        message.setQuestion(msg.getQuestion());
        message.setAnswer(msg.getAnswer());
        message.setUser(msg.getUser());

        return message;
    }

}
