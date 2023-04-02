package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.enumer.EventRole;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.AchievementRepository;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.MemberRepository;
import com.cyber.kitchen.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamService teamService;


    @Autowired
    SolutionService solutionService;

    @Autowired
    AchievementRepository achievementRepository;


    public String createEvent(User user, Event event, RedirectAttributes redirectAttributes){
        if (!user.getRoles().contains(Role.ORGANIZER))
            return "error404";

        if (findEventOrganizer(user) != null)
            return "error404";

        event.setExpertToken(UUID.randomUUID().toString());
        event.setMemberToken(UUID.randomUUID().toString());
        event.setOrganizer(userService.findUserById(user.getId()));
        event.setRunning(true);
        redirectAttributes.addAttribute("event", eventRepository.save(event));
        return "indexOrganizers";
    }

    public String enterToEventMember(User user, Long eventId, Model model, int page){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";


        if (getUsersFromMembers(event.getMembers()).contains(user)){
            Team team = getUsersTeamByEvent(event, user);
            model.addAttribute("event", event);
            model.addAttribute("member", userService.findMemberByUserAndEvent(user, event));


            if (event.getEndDate().isBefore(LocalDateTime.now()))
                return getEndTemplate(event, model);

            if (checkTeamForFits(event, userService.findMemberByUserAndEvent(user, event))){
                return "redirect:/";
            }

            if (page == 1) {
                if (team == null){
                    return "memberDashboardTeamSearch";
                }
                else {
                    if (LocalDateTime.now().isAfter(event.getStartDate()))
                        model.addAttribute("eventStarted", true);
                    model.addAttribute("team", team);
                    return "memberDashboardTeamProfile";
                }
            }
            else if (page == 2){
                if (LocalDateTime.now().isAfter(event.getStartDate())){
                    if (team.getTheme() != null)
                        model.addAttribute("themeIsSelected", true);
                    model.addAttribute("team", team);
                    return "memberDashboardTeamTheme";
                }else {
                    return "redirect:/event/member/" + event.getId() + "/teamProfile";
                }
            }else if (page == 3){
                if (LocalDateTime.now().isAfter(event.getStartDate()) && team != null && team.getTheme() != null) {
                    model.addAttribute("team", team);
                    model.addAttribute("tasks", event.getTaskList().stream().sorted(Comparator.comparing(Task::getStartDate)).collect(Collectors.toList()));
                    return "memberDashboardEventKanban";
                }else {
                    return "redirect:/event/member/" + event.getId() + "/teamProfile";
                }
            }

        }
        return "error404";
    }


    public String getEndTemplate(Event event, Model model){
        List<Team> teams = new ArrayList<>(event.getTeams().stream().sorted(Comparator.comparing((i) -> (solutionService.getTeamScore(event, i)))).toList());
        Collections.reverse(teams);

        if (event.getRunning()){
            endTheEvent(event, teams);
        }

        model.addAttribute("teams", teams);
        model.addAttribute("scores", solutionService.getTeamsScores(event, teams));
        return "DashboardEventEnding";
    }

    public void endTheEvent(Event event, List<Team> teams){

        for (Member member: event.getMembers()){
            Achievement achievement = new Achievement();
            achievement.setUser(member.getUser());
            achievement.setEvent(event);
            achievement.setName("Заняли " + teams.indexOf(teamRepository.findTeamById(teamRepository.findTeamByMember(member.getId()))));
            achievementRepository.save(achievement);
        }

        event.setRunning(false);
        eventRepository.save(event);
    }

    public Boolean checkTeamForFits(Event event, Member member){
        if (LocalDateTime.now().isAfter(event.getStartDate())){
            Long teamId = teamRepository.findTeamByMember(member.getId());
            if (teamId != null){
                Team team = teamRepository.findTeamById(teamId);
                if (team.getMembers().size() == event.getMaxMembersInTeam())
                    return Boolean.FALSE;
                for (Member memberOne: team.getMembers()){
                    team.getMembers().remove(memberOne);
                    event.getMembers().remove(memberOne);
                    eventRepository.save(event);
                    teamRepository.save(team);
                }
            }else {
                event.getMembers().remove(member);
                eventRepository.save(event);
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String enterToEventExpert(User user, Long eventId, Model model){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";

        if (event.getExperts().contains(user)){
            if (event.getEndDate().isBefore(LocalDateTime.now()))
                return getEndTemplate(event, model);

            model.addAttribute("event", event);
            model.addAttribute("tasks", event.getTaskList().stream().sorted(Comparator.comparing(Task::getStartDate)).collect(Collectors.toList()));
            model.addAttribute("teams", teamRepository.findTeamsByExpert(user.getId(), event.getId()).stream().map(i -> teamRepository.findTeamById(i)).collect(Collectors.toList()));
            return "expertDashboardEventKanban";
        }
        return "error404";
    }

    public String enterToEventOrganizer(User user, Long eventId, Model model, int page){
        Event event = eventRepository.findEventById(eventId);
        user = userService.findUserById(user.getId());

        if (event == null)
            return "error404";


        if (event.getOrganizer().equals(user)) {

            model.addAttribute("event", event);
            model.addAttribute("DateTimeFormatter", DateTimeFormatter.class);
            model.addAttribute("taskList", event.getTaskList().stream().sorted(Comparator.comparing(Task::getStartDate)).collect(Collectors.toList()));
            if (page == 1) {
                return "organizerDashboardEventProfile";
            }
            else if (page == 2) {
                return "organizerDashboardEventThemes";
            }
            else if (page == 3) {
                return "organizerDashboardEventTasks";
            }
            else if (page == 4) {
                return "organizerDashboardEventMembers";
            }
            else if (page == 5) {
                return "organizerDashboardEventExperts";
            }
            else if (page == 6) {
                List<Team> teams = new ArrayList<>(event.getTeams().stream().sorted(Comparator.comparing((i) -> (solutionService.getTeamScore(event, i)))).toList());
                Collections.reverse(teams);
                model.addAttribute("teams", teams);
                model.addAttribute("scores", solutionService.getTeamsScores(event, teams));
                return "organizerDashboardEventTeams";
            }
        }
        return "error404";
    }


    public String changeEvent(User user, Event newEvent, RedirectAttributes redirectAttributes, Long eventId){
        if (!user.getRoles().contains(Role.ORGANIZER))
            return "error404";

        Event eventBD = eventRepository.findEventById(eventId);

        if (eventBD == null)
            return "error404";
        if (!eventBD.getOrganizer().getId().equals(user.getId()))
            return "error404";

        eventBD.setEndDate(newEvent.getEndDate());
        eventBD.setStartDate(newEvent.getStartDate());
        eventBD.setMaxMembersInTeam(newEvent.getMaxMembersInTeam());
        redirectAttributes.addAttribute("event", eventRepository.save(eventBD));
        return "organizerDashboard";
    }

    public Team getUsersTeamByEvent(Event event, User user){
        Member member = userService.findMemberByUserAndEvent(user, event);
        for (Team team: event.getTeams())
            if (team.getMembers().contains(member))
                return team;

        return null;
    }

    public List<Event> findEventsByOrganizer(User user){
        return eventRepository.findEventsByOrganizer(userService.findUserById(user.getId()));
    }

    public List<User> getUsersFromMembers(List<Member> members){
        return members.stream().map(Member::getUser).collect(Collectors.toList());
    }


    public Event findEventByExpertToken(String token){
        return eventRepository.findEventByExpertToken(token);
    }

    public Event findEventByMemberToken(String token){
        return eventRepository.findEventByMemberToken(token);
    }

    public Event findEventOrganizer(User user){
        return eventRepository.findEventByOrganizer(user);
    }

    public String createTeam(User user, Team team, String role, RedirectAttributes redirectAttributes){
        user = userService.findUserById(user.getId());
        Event event = userService.findUsersCurrentEvent(user);
        Member member = userService.findMemberByUserAndEvent(user, event);

        member.setRole(userService.getEventRole(role));
        teamRepository.save(team);

        team.setLeader(member);
        team.getMembers().add(member);
        teamRepository.save(team);

        event.getTeams().add(team);
        eventRepository.save(event);
        return "redirect:/event/member/" + event.getId() + "/teamProfile";
    }

    public List<Team> getTeams(User user, String role){
        EventRole eventRole = userService.getEventRole(role);
        Event currentEvent = userService.findUsersCurrentEvent(user);
        List<Team> teams = currentEvent.getTeams();
        List<Team> availableTeams = new ArrayList<>();
        int memberSInTeam;

        for (Team team: teams){
            memberSInTeam = team.getMembers().size();
            Set<EventRole> roles = team.getMembers().stream().map(Member::getRole).collect(Collectors.toSet());

            // 1 место в команде
            if (currentEvent.getMaxMembersInTeam() == memberSInTeam + 1){
                if (roles.size() == 3 || roles.size() == 2 && !roles.contains(eventRole))
                    availableTeams.add(team);
            }

            // 2 места в команде
            else if (currentEvent.getMaxMembersInTeam() == memberSInTeam + 2){
                if (roles.size() == 3 || roles.size() == 2 || roles.size() == 1 && !roles.contains(eventRole))
                    availableTeams.add(team);
            }

            // 3 места в команде
            else if (currentEvent.getMaxMembersInTeam() > memberSInTeam + 2){
                availableTeams.add(team);
            }
        }

        return availableTeams;
    }


    public Event startEvent(User user){
        Event event = findEventOrganizer(user);
        event.setStartDate(LocalDateTime.now());
        event = eventRepository.save(event);

//        teamService.createTeams(event);
//        taskService.openTaskForAllTeams(user);

        return event;
    }

    @Transactional
    public Event save(Event event){
        return eventRepository.save(event);
    }

}
