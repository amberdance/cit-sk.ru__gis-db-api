package ru.hard2code.gisdbapi.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.hard2code.gisdbapi.domain.dto.MessageDto;
import ru.hard2code.gisdbapi.domain.entity.Message;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    Message toEntity(MessageDto messageDto);

    MessageDto toDto(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(ignore = true, target = "user")
    Message partialUpdate(MessageDto messageDto, @MappingTarget Message message);
}
