package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.ScheduleTask;
import com.lucinde.plannerpro.repositories.ScheduleTaskRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleTaskService {
    private final ScheduleTaskRepository scheduleTaskRepository;

    public ScheduleTaskService(ScheduleTaskRepository scheduleTaskRepository) {
        this.scheduleTaskRepository = scheduleTaskRepository;
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

    public ScheduleTaskDto transferScheduleTaskToDto(ScheduleTask scheduleTask) {
        ScheduleTaskDto scheduleTaskDto = new ScheduleTaskDto();

        scheduleTaskDto.id = scheduleTask.getId();
        scheduleTaskDto.date = scheduleTask.getDate();
        scheduleTaskDto.startTime = scheduleTask.getStartTime();
        scheduleTaskDto.endTime = scheduleTask.getEndTime();

        return scheduleTaskDto;
    }

    public ScheduleTask transferDtoToScheduleTask(ScheduleTaskDto scheduleTaskDto) {
        ScheduleTask scheduleTask = new ScheduleTask();

        // Geen setId nodig, deze genereert de database of staat in de URL
        scheduleTask.setDate(scheduleTaskDto.date);
        scheduleTask.setStartTime(scheduleTaskDto.endTime);
        scheduleTask.setEndTime(scheduleTaskDto.startTime);

        return scheduleTask;
    }
}
