package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    @Query("select u from user u where u.email = ?1")
    User findUserByEmail(String email);

    @Query(value = """
                     select e.id from jpa.event as e, jpa.member as m, jpa.event_members as col\s
                        where col.event_id = e.id and col.members_id = m.id and m.user_id = ?1 and e.running = true;
                        """, nativeQuery = true)
    Long findUsersCurrentEvent(Long userId);

    @Query(value = """
                     select e.id from jpa.event as e, jpa.member as m, jpa.event_members as col\s
                        where col.event_id = e.id and col.members_id = m.id and m.user_id = 452;
                        """, nativeQuery = true)
    Long findUsersEvents(User user);
}
