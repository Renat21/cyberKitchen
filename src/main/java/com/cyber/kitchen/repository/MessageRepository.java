package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.Message;
import com.cyber.kitchen.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findMessageById(Long id);

    List<Message> findMessagesBySolution(Solution solution);
}
