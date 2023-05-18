package ru.hard2code.gisdbapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.gisdbapi.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
