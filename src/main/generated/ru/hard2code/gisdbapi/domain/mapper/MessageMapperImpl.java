package ru.hard2code.gisdbapi.domain.mapper;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hard2code.gisdbapi.domain.dto.MessageDto;
import ru.hard2code.gisdbapi.domain.dto.UserDto;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.domain.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-19T14:32:55+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Message toEntity(MessageDto messageDto) {
        if ( messageDto == null ) {
            return null;
        }

        Message message = new Message();

        message.setId( messageDto.getId() );
        message.setQuestion( messageDto.getQuestion() );
        message.setAnswer( messageDto.getAnswer() );
        message.setUser( userMapper.toEntity( messageDto.getUser() ) );

        return message;
    }

    @Override
    public MessageDto toDto(Message message) {
        if ( message == null ) {
            return null;
        }

        Long id = null;
        String question = null;
        String answer = null;
        UserDto user = null;

        id = message.getId();
        question = message.getQuestion();
        answer = message.getAnswer();
        user = userMapper.toDto( message.getUser() );

        MessageDto messageDto = new MessageDto( id, question, answer, user );

        return messageDto;
    }

    @Override
    public Message partialUpdate(MessageDto messageDto, Message message) {
        if ( messageDto == null ) {
            return null;
        }

        if ( messageDto.getId() != null ) {
            message.setId( messageDto.getId() );
        }
        if ( messageDto.getQuestion() != null ) {
            message.setQuestion( messageDto.getQuestion() );
        }
        if ( messageDto.getAnswer() != null ) {
            message.setAnswer( messageDto.getAnswer() );
        }
        if ( messageDto.getUser() != null ) {
            if ( message.getUser() == null ) {
                message.setUser( new User() );
            }
            userMapper.partialUpdate( messageDto.getUser(), message.getUser() );
        }

        return message;
    }
}
