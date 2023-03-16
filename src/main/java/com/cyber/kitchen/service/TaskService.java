package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.enumer.State;
import com.cyber.kitchen.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EventService eventService;

    @Autowired
    SolutionService solutionService;

    public String createNewTask(User organizer, Task task){
        Event event = eventService.findEventOrganizer(organizer);
        List<Task> taskList = event.getTaskList();
        task.setNumeration((long) taskList.size());
        taskRepository.save(task);

        taskList.add(task);
        event.setTaskList(taskList);
        eventService.save(event);
        return "organizerDashboard";
    }

    public String changeTask(User organizer, Long taskId, Task newTask, List<Long> numerations){
        Task task = taskRepository.findTaskById(taskId);
        task.setDescription(newTask.getDescription());
        task.setName(newTask.getName());
        task.setMaxScore(newTask.getMaxScore());
        task.setStartDate(newTask.getStartDate());
        task.setEndDate(newTask.getEndDate());
        taskRepository.save(task);

        changeNumerations(organizer, numerations);
        return "organizerDashboard";
    }

    public void changeNumerations(User organizer, List<Long> numerations){
        Event event = eventService.findEventOrganizer(organizer);
        List<Task> taskList = event.getTaskList();
        for (Task task : taskList) {
            task.setNumeration(numerations.get(numerations.indexOf(task.getId())));
            taskRepository.save(task);
        }
    }

    public List<Task> getAllTasks(User organizer){
        return eventService.findEventOrganizer(organizer).getTaskList();
    }

    public List<Task> getAllTasksSorted(User organizer){
        return eventService.findEventOrganizer(organizer).getTaskList().stream().sorted(Comparator.comparing(Task::getNumeration)).collect(Collectors.toList());
    }

    public void openTaskForAllTeams(User organizer){
        Event event = eventService.findEventOrganizer(organizer);
        for (Team team: event.getTeams())
            openTaskForTeam(team, event);
    }

    public void openTaskForTeam(Team team, Event event){
        Solution solution = new Solution();
        solution.setCurScore(0L);
        solution.setTask(getAllTasksSorted(event.getOrganizer()).get(solutionService.getSolutionsByTeam(team).size()));
        solution.setState(State.STARTED);
        solution.setTeam(team);
        solutionService.save(solution);
    }
}
