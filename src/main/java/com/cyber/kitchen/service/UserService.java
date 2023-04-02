package com.cyber.kitchen.service;

import com.cyber.kitchen.entity.Event;
import com.cyber.kitchen.entity.Member;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.EventRole;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.repository.AchievementRepository;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.MemberRepository;
import com.cyber.kitchen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class userService - класс для основных операций над пользователем
 **/
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

//    @Autowired
//    ImageService imageService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    MemberRepository memberRepository;

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findUserById(Long id){
        return userRepository.findUserById(id);
    }

    public String registerUser(User user, String confirmPassword, String yourRole, RedirectAttributes redirectAttributes){


        if (!Objects.equals(user.getPassword(), confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordConfirm", true);
            return "redirect:/registration";
        }
        if (findUserByUsername(user.getUsername()) != null) {
            redirectAttributes.addFlashAttribute("username", true);
            return "redirect:/registration";
        }
        if (findUserByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("email", true);
            return "redirect:/registration";
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(getOneRole(yourRole)));
        saveUser(user);
        return "redirect:/login";
    }

    public Role getOneRole(String role){
        if (Role.USER.name().equals(role))
            return Role.USER;
        else if (Role.EXPERT.name().equals(role))
            return Role.EXPERT;
        else if (Role.ORGANIZER.name().equals(role))
            return Role.ORGANIZER;
        return Role.USER;
    }

    public EventRole getEventRole(String role){
        if (EventRole.DESIGNER.name().equals(role))
            return EventRole.DESIGNER;
        else if (EventRole.DEVELOPER.name().equals(role))
            return EventRole.DEVELOPER;
        else if (EventRole.MANAGER.name().equals(role))
            return EventRole.MANAGER;
        return EventRole.DEVELOPER;
    }

    public Event findUsersCurrentEvent(User user){
        return eventRepository.findEventById(userRepository.findUsersCurrentEvent(user.getId()));
    }

    public Event findExpertsCurrentEvent(User user){
        return eventRepository.findEventById(userRepository.findExpertsCurrentEvent(user.getId()));
    }

    public String getMembersProfile(User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("achievements", achievementRepository.findAchievementsByUser(user));
        model.addAttribute("userService", this);
        model.addAttribute("DateTimeFormatter", DateTimeFormatter.class);
        return "membersProfile";
    }


    public String changeMembersProfile(User user, User newUser) {

        user.setName(newUser.getName());
        user.setAddress(newUser.getSurname());
        user.setGit(newUser.getGit());
        saveUser(user);
        return "redirect:/profile/member";
    }

    public String changeExpertsProfile(User user, User newUser) {

        user.setName(newUser.getName());
        user.setAddress(newUser.getSurname());
        user.setGit(newUser.getGit());
        saveUser(user);
        return "redirect:/profile/expert";
    }

    public List<Event> getEventsByExpert(User user){
        return eventRepository.findAll().stream().filter(i -> i.getExperts().stream().map(User::getId).toList().contains(user.getId())).toList();
    }

    public String getExpertsProfile(User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("events", getEventsByExpert(user));
        model.addAttribute("DateTimeFormatter", DateTimeFormatter.class);
        return "expertsProfile";
    }


    public String changePassword(User user, String currentPassword, String newPassword,
                                 String passwordConfirm, RedirectAttributes redirectAttributes) {
        if (!Objects.equals(currentPassword, "") && !Objects.equals(newPassword, "")
                && !Objects.equals(passwordConfirm, "")) {
            if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
                redirectAttributes.addFlashAttribute("errorCurrentPassword", true);
                return "redirect:/profile";
            }

            if (!Objects.equals(newPassword, passwordConfirm)) {
                redirectAttributes.addFlashAttribute("errorPassword", true);
                return "redirect:/profile";
            }

            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            redirectAttributes.addFlashAttribute("passwordChanged", true);
            saveUser(user);

        }
        return "redirect:/profile";
    }

//    public String changePhoto(RedirectAttributes redirectAttributes,
//                              MultipartFile file, @AuthenticationPrincipal User user) throws IOException {
//        imageService.saveImage(file, user);
//        redirectAttributes.addFlashAttribute("photoChanged", true);
//        return "redirect:/profile";
//    }


    @Transactional
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    @Transactional
    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Member findMemberByUserAndEvent(User user, Event event){
        for (Member cMember: event.getMembers())
            if (cMember.getUser().getId().equals(user.getId()))
                return cMember;
        return null;
    }

    @Transactional
    public User getUserAuth() {

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
