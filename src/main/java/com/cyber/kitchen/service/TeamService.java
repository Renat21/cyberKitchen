package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    public void createTeams(Event event){
        List<Member> memberList = event.getMembers().stream().sorted(Comparator.comparing(Member::getEntranceDate)).toList();

        Team team = new Team();

    }
}
