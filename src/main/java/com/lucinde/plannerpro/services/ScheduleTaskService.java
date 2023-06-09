package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.ScheduleTask;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.repositories.ScheduleTaskRepository;
import com.lucinde.plannerpro.repositories.TaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleTaskService {
    private final ScheduleTaskRepository scheduleTaskRepository;
    private final TaskRepository taskRepository;

    public ScheduleTaskService(ScheduleTaskRepository scheduleTaskRepository, TaskRepository taskRepository) {
        this.scheduleTaskRepository = scheduleTaskRepository;
        this.taskRepository = taskRepository;
    }

    public List<ScheduleTaskDto> getAllScheduleTasks() {
        Iterable<ScheduleTask> scheduleTasks = scheduleTaskRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<ScheduleTaskDto> scheduleTaskDtos = new ArrayList<>();

        for (ScheduleTask st: scheduleTasks) {
            scheduleTaskDtos.add(transferScheduleTaskToDto(st));
        }

        return scheduleTaskDtos;
    }

    public ScheduleTaskDto getScheduleTask(Long id) {
        Optional<ScheduleTask> scheduleTaskOptional = scheduleTaskRepository.findById(id);

        if(scheduleTaskOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen ingeplande taak gevonden met id: " + id);
        }

        ScheduleTask scheduleTask = scheduleTaskOptional.get();

        return transferScheduleTaskToDto(scheduleTask);
    }

    public ScheduleTaskDto addScheduleTask(ScheduleTaskDto scheduleTaskDto) {
        ScheduleTask scheduleTask = transferDtoToScheduleTask(scheduleTaskDto);
        //todo: controle toevoegen of de tijden kloppen en niet overlappen bij monteurs of tijden
        scheduleTaskRepository.save(scheduleTask);

        return transferScheduleTaskToDto(scheduleTask);
    }

    public ScheduleTaskDto updateScheduleTask(Long id, ScheduleTaskDto scheduleTaskDto) {
        Optional<ScheduleTask> scheduleTaskOptional = scheduleTaskRepository.findById(id);
        if(scheduleTaskOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen ingeplande taak gevonden met id: " + id);
        }
        //todo: controle toevoegen of tijden kloppen en niet overlappen bij monteurs en tijden (het id dat je wilt aanpassen uitsluiten! - optionele parameters? / Optional voor laatste param (standaard null? isPresent check))

        ScheduleTask updateScheduleTask = transferDtoToScheduleTask(scheduleTaskDto);
        updateScheduleTask.setId(id);
        scheduleTaskRepository.save(updateScheduleTask);

        return transferScheduleTaskToDto(updateScheduleTask);
    }

    public ScheduleTaskDto assignScheduleToTask(Long id, Long task_id) {
        // in plaats van een aparte optional aan te maken heb ik hier de optie gebruikt om meteen een exception te gooien als het ID niet bestaat
        ScheduleTask scheduleTask = scheduleTaskRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Geen planning gevonden met id: " + id));
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RecordNotFoundException("Geen taak gevonden met id: " + task_id));

        scheduleTask.setTask(task);
        scheduleTaskRepository.save(scheduleTask);

        return transferScheduleTaskToDto(scheduleTask);
    }

    public void deleteScheduleTask(Long id) {
        Optional<ScheduleTask> optionalScheduleTask = scheduleTaskRepository.findById(id);
        if(optionalScheduleTask.isEmpty()) {
            throw new RecordNotFoundException("Geen ingeplande taak gevonden met id: " + id);
        }
        scheduleTaskRepository.deleteById(id);
    }

    public ScheduleTaskDto transferScheduleTaskToDto(ScheduleTask scheduleTask) {
        ScheduleTaskDto scheduleTaskDto = new ScheduleTaskDto();

        scheduleTaskDto.id = scheduleTask.getId();
        scheduleTaskDto.date = scheduleTask.getDate();
        scheduleTaskDto.startTime = scheduleTask.getStartTime();
        scheduleTaskDto.endTime = scheduleTask.getEndTime();
        scheduleTaskDto.task = scheduleTask.getTask();

        return scheduleTaskDto;
    }

    public ScheduleTask transferDtoToScheduleTask(ScheduleTaskDto scheduleTaskDto) {
        ScheduleTask scheduleTask = new ScheduleTask();

        // Geen setId nodig, deze genereert de database of staat in de URL
        scheduleTask.setDate(scheduleTaskDto.date);
        scheduleTask.setStartTime(scheduleTaskDto.endTime);
        scheduleTask.setEndTime(scheduleTaskDto.startTime);
        scheduleTask.setTask(scheduleTaskDto.task);

        return scheduleTask;
    }
}

