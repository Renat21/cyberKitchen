package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.MemberRepository;
import com.cyber.kitchen.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    UserService userService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EventService eventService;

    public String memberEntranceEvent(User user, String token){
        Event event = eventService.findEventByMemberToken(token);
        if (event == null)
            return "error404";

        if (eventService.getUsersFromMembers(event.getMembers()).contains(user))
            return "error404";

        Member member = new Member();
        member.setEntranceDate(LocalDateTime.now());
        member.setUser(userService.findUserById(user.getId()));
        memberRepository.save(member);

        List<Member> members = event.getMembers();
        members.add(member);
        eventService.save(event);
        return "redirect:/event/" + event.getId();
    }


}
