package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.TaskDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.exceptions.RelationFoundException;
import com.lucinde.plannerpro.models.ScheduleTask;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> getAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        List<TaskDto> taskDtos = new ArrayList<>();

        for (Task t: tasks) {
            taskDtos.add(transferTaskToDto(t));
        }

        return taskDtos;
    }

    public TaskDto getTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen taak gevonden met id: " + id);
        }

        Task task = taskOptional.get();

        return transferTaskToDto(task);
    }

    public TaskDto addTask(TaskDto taskDto) {
        Task task = transferDtoToTask(taskDto);
        taskRepository.save(task);

        return transferTaskToDto(task);
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if(taskOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen taak gevonden met id: " + id);
        }
//        Task existingTask = taskOptional.get();
        Task updateTask = transferDtoToTask(taskDto, taskOptional.get());
        updateTask.setId(id);

        taskRepository.save(updateTask);

        return transferTaskToDto(updateTask);
    }

    public void deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isEmpty()) {
            throw new RecordNotFoundException("Geen taak gevonden met id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskDto transferTaskToDto(Task task) {
        TaskDto taskDto = new TaskDto();

        taskDto.id = task.getId();
        taskDto.description = task.getDescription();
        taskDto.workPerformed = task.getWorkPerformed();
        taskDto.jobDone = task.getJobDone();
        taskDto.customer = task.getCustomer();
        taskDto.fileList = task.getFileList();
        taskDto.scheduleTaskList = task.getScheduleTaskList();

        return taskDto;
    }

    public Task transferDtoToTask(TaskDto taskDto) {
        return transferDtoToTask(taskDto, null);
    }

    public Task transferDtoToTask(TaskDto taskDto, Task existingTask) {

        Task task = new Task();

        // Geen setId nodig, deze genereert de database of staat in de URL
        task.setDescription(taskDto.description);
        task.setWorkPerformed(taskDto.workPerformed);
        task.setJobDone(taskDto.jobDone);
        if(taskDto.customer != null)
            task.setCustomer(taskDto.customer);
        else
            if(existingTask.getCustomer() != null)
                task.setCustomer(existingTask.getCustomer());
        if(taskDto.scheduleTaskList != null)
            task.setFileList(taskDto.fileList);
        else
            if(existingTask.getCustomer() != null)
                task.setFileList(existingTask.getFileList());
        if(taskDto.scheduleTaskList != null)
            task.setScheduleTaskList(taskDto.scheduleTaskList);
        else
            if(existingTask.getScheduleTaskList() != null)
                task.setScheduleTaskList(existingTask.getScheduleTaskList());

        return task;
    }
}
