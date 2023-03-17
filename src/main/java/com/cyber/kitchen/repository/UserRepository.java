package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    User findUserByEmail(String email);

    @Query(value = "select u from jpa.event, jpa. u where u.username = ?1", nativeQuery = true)
    User findUserByUsername(String name);
}
