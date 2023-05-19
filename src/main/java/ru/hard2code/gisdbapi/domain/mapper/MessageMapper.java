package ru.hard2code.gisdbapi.domain.mapper;

import org.mapstruct.*;
import ru.hard2code.gisdbapi.domain.dto.MessageDto;
import ru.hard2code.gisdbapi.domain.entity.Message;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    Message toEntity(MessageDto messageDto);

    MessageDto toDto(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Message partialUpdate(MessageDto messageDto, @MappingTarget Message message);
}
