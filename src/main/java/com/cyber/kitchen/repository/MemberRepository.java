package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberById(Long id);

    Member findMemberByUser(User user);

}
