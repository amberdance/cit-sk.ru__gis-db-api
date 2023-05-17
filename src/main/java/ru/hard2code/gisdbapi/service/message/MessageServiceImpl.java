package ru.hard2code.gisdbapi.service.message;

import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Message;
import ru.hard2code.gisdbapi.model.user.Role;
import ru.hard2code.gisdbapi.repository.MessageRepository;
import ru.hard2code.gisdbapi.repository.UserRoleRepository;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    private final UserService userService;
    private final UserRoleRepository userRoleRepository;

    public MessageServiceImpl(MessageRepository messageRepository,
                              UserService userService,
                              UserRoleRepository userRoleRepository) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
    }


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

        if (user.getRole() != null && user.getRole().getId() != null) {
            user.setRole(userRoleRepository.findById(user.getRole().getId())
                    .orElseThrow(() -> new EntityNotFoundException(Role.class, user.getRole().getId())));
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
    public Message updateMessage(Message msg) {
        var message = getMessageById(msg.getId());
        message.setUser(msg.getUser());
        message.setLabel(msg.getLabel());
        message.setAnswer(msg.getAnswer());

        return message;
    }


}
