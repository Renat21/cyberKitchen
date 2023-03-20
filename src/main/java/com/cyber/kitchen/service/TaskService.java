package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.enumer.State;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EventService eventService;

    @Autowired
    SolutionService solutionService;

    @Autowired
    EventRepository eventRepository;

    public String createNewTask(User organizer, Task task){
        Event event = eventService.findEventOrganizer(organizer);
        List<Task> taskList = event.getTaskList();
        task.setNumeration((long) taskList.size() + 1);
        taskRepository.save(task);

        taskList.add(task);
        event.setTaskList(taskList);
        eventService.save(event);
        return "redirect:/event/organizer/" + event.getId() + "/tasks";
    }

    public String changeTask(User organizer, Task newTask, Long taskId){
        Event event = eventService.findEventOrganizer(organizer);

        Task task = taskRepository.findTaskById(taskId);
        task.setDescription(newTask.getDescription());
        task.setName(newTask.getName());
        task.setMaxScore(newTask.getMaxScore());
        taskRepository.save(task);

        return "redirect:/event/organizer/" + event.getId() + "/tasks";
    }

    public String changeNumerations(User organizer, Map<Long, Long> numerations){
        Event event = eventService.findEventOrganizer(organizer);
        List<Task> taskList = event.getTaskList();

        for (Task task : taskList) {
            task.setNumeration(numerations.get(task.getId()) + 1);
            taskRepository.save(task);
        }

        return "success";
    }

    public void updateNumeration(List<Task> taskList){
        for (Task task: taskList){
            task.setNumeration(taskList.indexOf(task) + 1L);
            taskRepository.save(task);
        }
    }

    public String deleteTaskForEvent(User user, Long taskId){
        Event event = eventService.findEventOrganizer(user);
        Task task = taskRepository.findTaskById(taskId);

        if (task != null){
            event.getTaskList().remove(task);
            eventRepository.save(event);
            taskRepository.delete(task);

            updateNumeration(getAllTasksSorted(user));

            return "redirect:/event/organizer/" + event.getId() + "/tasks";
        }

        return "error404";
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
