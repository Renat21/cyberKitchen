package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findTeamById(Long id);

    @Query(value = """
             select t.id from jpa.team as t, jpa.team_members as tm 
                where ?1 = tm.member_id and tm.team_id = t.id
            """, nativeQuery = true)
    Long findTeamByMember(Long member);

    List<Team> findTeamsByExpert(User user);
}
