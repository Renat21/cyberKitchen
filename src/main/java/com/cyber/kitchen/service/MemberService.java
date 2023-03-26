package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.enumer.EventRole;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.MemberRepository;
import com.cyber.kitchen.repository.TeamRepository;
import com.cyber.kitchen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    UserService userService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventService eventService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    SolutionService solutionService;

    public String memberEntranceEvent(User user, String token){
        Event event = eventService.findEventByMemberToken(token);
        if (event == null)
            return "error404";

        if (eventService.getUsersFromMembers(event.getMembers()).contains(user))
            return "error404";

        if (LocalDateTime.now().isAfter(event.getStartDate())){
            return "error404";
        }

        Member member = new Member();
        member.setEntranceDate(LocalDateTime.now());
        member.setUser(userService.findUserById(user.getId()));
        memberRepository.save(member);

        List<Member> members = event.getMembers();
        members.add(member);
        eventService.save(event);
        return "redirect:/event/member/" + event.getId() + "/teamProfile";
    }

    public String exitFromTeam(User user, RedirectAttributes redirectAttributes){
        Event event = userService.findUsersCurrentEvent(user);
        Member member = memberRepository.findMemberByUser(user);
        Team team = teamRepository.findTeamById(teamRepository.findTeamByMember(member.getId()));
        team.getMembers().remove(member);

        member.setTeamName(null);
        memberRepository.save(member);

        if (team.getMembers().size() > 0){
            team.setLeader(team.getMembers().get(0));
        }

        teamRepository.save(team);

        if (team.getMembers().size() == 0) {
            event.getTeams().remove(team);
            eventRepository.save(event);
            teamRepository.delete(team);
        }


        return "redirect:/event/member/" + event.getId() + "/teamProfile";
    }

    public String addToTeam(User user, String username, String role, RedirectAttributes redirectAttributes){
        Event event = userService.findUsersCurrentEvent(user);
        Member member = memberRepository.findMemberByUser(user);
        Team team = teamRepository.findTeamById(teamRepository.findTeamByMember(member.getId()));
        EventRole eventRole = userService.getEventRole(role);

        User userToAdd = userService.findUserByUsername(username);
        if (userToAdd == null) {
            redirectAttributes.addFlashAttribute("errorEvent", true);
            return "redirect:/event/member/" + event.getId() + "/teamProfile";
        }

        Member memberToAdd = memberRepository.findMemberByUser(userToAdd);
        if (memberToAdd == null) {
            redirectAttributes.addFlashAttribute("errorEvent", true);
            return "redirect:/event/member/" + event.getId() + "/teamProfile";
        }

        if (team.getMembers().size() == event.getMaxMembersInTeam()){
            redirectAttributes.addFlashAttribute("errorPlace", true);
            return "redirect:/event/member/" + event.getId() + "/teamProfile";
        }

        if (teamRepository.findTeamByMember(memberToAdd.getId()) != null){
            redirectAttributes.addFlashAttribute("errorTeam", true);
            return "redirect:/event/member/" + event.getId() + "/teamProfile";
        }

        if (!checkForCompatibility(event, team, eventRole)){
            redirectAttributes.addFlashAttribute("errorRole", true);
            return "redirect:/event/member/" + event.getId() + "/teamProfile";
        }

        return "redirect:" + teamService.enterToTeam(userToAdd, team.getId(), role);
    }

    public Boolean checkForCompatibility(Event event, Team team, EventRole eventRole){
        int memberSInTeam = team.getMembers().size();
        Set<EventRole> roles = team.getMembers().stream().map(Member::getRole).collect(Collectors.toSet());

        // 1 место в команде
        if (event.getMaxMembersInTeam() == memberSInTeam + 1){
            return roles.size() == 3 || roles.size() == 2 && !roles.contains(eventRole);
        }

        // 2 места в команде
        else if (event.getMaxMembersInTeam() == memberSInTeam + 2){
            return roles.size() == 3 || roles.size() == 2 || roles.size() == 1 && !roles.contains(eventRole);
        }

        // 3 места в команде
        else return event.getMaxMembersInTeam() > memberSInTeam + 2;
    }
    
    public List<Solution> getKanbanBoard(User user){
        Team team = teamRepository.findTeamById(teamRepository.findTeamByMember(memberRepository.findMemberByUser(user).getId()));
        if (team == null)
            return null;
        
        return solutionService.getSolutionsByTeam(team);
    }


}
