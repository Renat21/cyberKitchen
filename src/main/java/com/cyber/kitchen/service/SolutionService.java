package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.Message;
import com.cyber.kitchen.entity.Solution;
import com.cyber.kitchen.entity.Team;
import com.cyber.kitchen.entity.User;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.enumer.State;
import com.cyber.kitchen.repository.MessageRepository;
import com.cyber.kitchen.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolutionService {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    MessageRepository messageRepository;

    public List<Solution> getSolutionsByTeam(Team team){
        return solutionRepository.findSolutionsByTeam(team);
    }

    public void sendSolutionForCheck(User user, Long solutionId, Message message){
        Solution solution = solutionRepository.findSolutionById(solutionId);

        message.setUser(user);
        message.setSolution(solution);

        messageRepository.save(message);

        if (!user.getRoles().contains(Role.EXPERT)) {
            solution.setState(State.REVIEW);
            solutionRepository.save(solution);
        }
    }

    public List<Message> getAllMessagesBySolution(Long solutionId){
        Solution solution = solutionRepository.findSolutionById(solutionId);
        return messageRepository.findMessagesBySolution(solution);
    }

    public State getSolutionState(String state){
        if (State.NOT_STARTED.name().equals(state))
            return State.NOT_STARTED;
        else if (State.IN_PROGRESS.name().equals(state))
            return State.IN_PROGRESS;
        else if (State.REVIEW.name().equals(state))
            return State.REVIEW;
        else if(State.CLOSED.name().equals(state))
            return State.CLOSED;
        return State.NOT_STARTED;
    }

    public void setSolutionState(User user, Long solutionId, String state){
        Solution solution = solutionRepository.findSolutionById(solutionId);

        solution.setState(getSolutionState(state));

        solutionRepository.save(solution);

    }


    @Transactional
    public Solution save(Solution solution){
        return solutionRepository.save(solution);
    }
}
