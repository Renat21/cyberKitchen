package com.cyber.kitchen.repository;

import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository  extends JpaRepository<Event, Long> {
    Event findEventById(Long id);
    Event findEventByExpertToken(String token);

    Event findEventByMemberToken(String token);
    @Query("select e from Event e where e.organizer = ?1 and e.running = true")
    Event findEventByOrganizer(User user);
}
