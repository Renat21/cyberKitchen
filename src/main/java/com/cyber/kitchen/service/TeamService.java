package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.EventRole;
import com.cyber.kitchen.repository.MemberRepository;
import com.cyber.kitchen.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserService userService;

    public String enterToTeam(User user, Long teamId, String role){
        Event event = userService.findUsersCurrentEvent(user);
        EventRole eventRole = userService.getEventRole(role);
        Member member = memberRepository.findMemberByUser(user);
        Team team = teamRepository.findTeamById(teamId);

        member.setRole(eventRole);
        member.setTeamName(team.getName());
        memberRepository.save(member);


        team.getMembers().add(member);
        teamRepository.save(team);

        return "/event/member/" + event.getId() + "/teamProfile";
    }
}
