package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.enumer.Role;
import com.cyber.kitchen.enumer.State;
import com.cyber.kitchen.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolutionService {
    @Autowired
    SolutionRepository solutionRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @Autowired
    DocumentRepository documentRepository;

    public List<Solution> getSolutionsByTeam(User user, Team team){
        List<Task> taskList = userService.findUsersCurrentEvent(user).getTaskList();
        for (Task task: taskList){
            if (LocalDateTime.now().isAfter(task.getStartDate()))
                openTaskForTeam(team, task);
        }
        return solutionRepository.findSolutionsByTeam(team);
    }

    public List<Solution> getSolutionsByTeam(Event event, Team team){
        List<Task> taskList = event.getTaskList();
        for (Task task: taskList){
            if (LocalDateTime.now().isAfter(task.getStartDate()))
                openTaskForTeam(team, task);
        }
        return solutionRepository.findSolutionsByTeam(team);
    }

    public List<Solution> getSolutionsByTeamExpert(User user, Team team){
        List<Task> taskList = userService.findExpertsCurrentEvent(user).getTaskList();
        for (Task task: taskList){
            if (LocalDateTime.now().isAfter(task.getStartDate()))
                openTaskForTeam(team, task);
        }
        return solutionRepository.findSolutionsByTeam(team);
    }

    public void openTaskForTeam(Team team, Task task){
        if (solutionRepository.findSolutionByTaskAndTeam(task, team) == null) {
            Solution solution = new Solution();
            solution.setCurScore(0L);
            solution.setTask(task);
            solution.setState(State.NOT_STARTED);
            solution.setTeam(team);
            solutionRepository.save(solution);
        }
    }

    public Message sendSolutionForCheck(User user, Long solutionId, String data, List<Number> filesId){
        Solution solution = solutionRepository.findSolutionById(solutionId);

        Message message = new Message();
        message.setData(data);
        message.setUser(user);
        message.setSolution(solution);

        List<Document> documents = new ArrayList<>();

        if (filesId != null) {
            for (Number fileId : filesId) {
                documents.add(documentRepository.findDocumentById(fileId.longValue()));
            }
        }
        message.setDocuments(documents);



        messageRepository.save(message);

        if (!user.getRoles().contains(Role.EXPERT) && !solution.getState().equals(State.CLOSED)) {
            solution.setState(State.REVIEW);
            solutionRepository.save(solution);
        }

        return message;
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

    public Long setSolutionState(Long solutionId, String state, Long curScore){
        Solution solution = solutionRepository.findSolutionById(solutionId);

        solution.setState(getSolutionState(state));

        if (curScore > 0)
            solution.setCurScore(curScore);

        solutionRepository.save(solution);

        return solution.getCurScore();
    }

    public Solution getSolutionById(Long id){
        return solutionRepository.findSolutionById(id);
    }

    public Long getTeamScore(Event event, Team team){
        List<Solution> solutions = getSolutionsByTeam(event, team);
        return solutions.stream().mapToLong(Solution::getCurScore).sum();
    }

    public Map<Long, Long> getTeamsScores(Event event, List<Team> teams){
        return teams.stream().collect(Collectors.toMap(Team::getId, value -> getTeamScore(event, value)));
    }


    @Transactional
    public Solution save(Solution solution){
        return solutionRepository.save(solution);
    }
}
