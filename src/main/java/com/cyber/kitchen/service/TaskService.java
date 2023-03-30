package com.cyber.kitchen.service;


import com.cyber.kitchen.entity.*;
import com.cyber.kitchen.enumer.State;
import com.cyber.kitchen.repository.EventRepository;
import com.cyber.kitchen.repository.SolutionRepository;
import com.cyber.kitchen.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    SolutionRepository solutionRepository;

    @Autowired
    EventRepository eventRepository;

    public String createNewTask(User organizer, Task task){
        Event event = eventService.findEventOrganizer(organizer);
        List<Task> taskList = event.getTaskList();
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
        task.setStartDate(newTask.getStartDate());
        taskRepository.save(task);

        return "redirect:/event/organizer/" + event.getId() + "/tasks";
    }

    public List<Task> changeNumerations(User organizer, Map<Long, Long> numerations){
        Event event = eventService.findEventOrganizer(organizer);
        List<Task> taskList = event.getTaskList().stream().sorted(Comparator.comparing(Task::getStartDate)).toList();
        List<LocalDateTime> startDates = taskList.stream().map(Task::getStartDate).toList();


        for (Task task : taskList) {
            task.setStartDate(startDates.get(Math.toIntExact(numerations.get(task.getId()))));
            taskRepository.save(task);
        }

        return event.getTaskList().stream().sorted(Comparator.comparing(Task::getStartDate)).toList();
    }

    public String deleteTaskForEvent(User user, Long taskId){
        Event event = eventService.findEventOrganizer(user);
        Task task = taskRepository.findTaskById(taskId);

        if (task != null){
            event.getTaskList().remove(task);
            eventRepository.save(event);
            taskRepository.delete(task);

            return "redirect:/event/organizer/" + event.getId() + "/tasks";
        }

        return "error404";
    }

    public List<Task> getAllTasks(User organizer){
        return eventService.findEventOrganizer(organizer).getTaskList();
    }


    public List<Task> getAllTasksSortedByStartDate(User organizer){
        return eventService.findEventOrganizer(organizer).getTaskList().stream().sorted(Comparator.comparing(Task::getStartDate)).collect(Collectors.toList());
    }



}
