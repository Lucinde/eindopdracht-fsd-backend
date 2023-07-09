package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.ScheduleTaskInputDto;
import com.lucinde.plannerpro.dtos.ScheduleTaskOutputDto;
import com.lucinde.plannerpro.exceptions.BadRequestException;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.exceptions.RelationFoundException;
import com.lucinde.plannerpro.models.ScheduleTask;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.models.User;
import com.lucinde.plannerpro.repositories.ScheduleTaskRepository;
import com.lucinde.plannerpro.repositories.TaskRepository;
import com.lucinde.plannerpro.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ScheduleTaskService {
    private final ScheduleTaskRepository scheduleTaskRepository;
    private final TaskRepository taskRepository;

    public ScheduleTaskService(ScheduleTaskRepository scheduleTaskRepository, TaskRepository taskRepository) {
        this.scheduleTaskRepository = scheduleTaskRepository;
        this.taskRepository = taskRepository;
    }

    public List<ScheduleTaskOutputDto> getAllScheduleTasks() {
        Iterable<ScheduleTask> scheduleTasks = scheduleTaskRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<ScheduleTaskOutputDto> scheduleTaskOutputDtos = new ArrayList<>();

        for (ScheduleTask st: scheduleTasks) {
            scheduleTaskOutputDtos.add(transferScheduleTaskToOutputDto(st));
        }

        return scheduleTaskOutputDtos;
    }

    public ScheduleTaskOutputDto getScheduleTask(Long id) {
        Optional<ScheduleTask> scheduleTaskOptional = scheduleTaskRepository.findById(id);

        if(scheduleTaskOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen ingeplande taak gevonden met id: " + id);
        }

        ScheduleTask scheduleTask = scheduleTaskOptional.get();

        return transferScheduleTaskToOutputDto(scheduleTask);
    }

    public PageResponse<ScheduleTaskOutputDto> getScheduleTaskWithPagination(int pageNo, int pageSize, boolean includeOlderTasks) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending());
        Page<ScheduleTask> pagingScheduleTask;
        LocalDate currentDate = LocalDate.now();

        if(includeOlderTasks) {
            pagingScheduleTask = scheduleTaskRepository.findAll(pageRequest);
        } else {
            pagingScheduleTask = scheduleTaskRepository.findAllByDateAfter(currentDate, pageRequest);
        }

        PageResponse<ScheduleTaskOutputDto> response = new PageResponse<>();

        response.count = pagingScheduleTask.getTotalElements();
        response.totalPages = pagingScheduleTask.getTotalPages();
        response.hasNext = pagingScheduleTask.hasNext();
        response.hasPrevious = pagingScheduleTask.hasPrevious();
        response.items = new ArrayList<>();

        for (ScheduleTask t : pagingScheduleTask) {
            response.items.add(transferScheduleTaskToOutputDto(t));
        }

        return response;
    }

    public PageResponse<ScheduleTaskOutputDto> getScheduleTasksByMechanicWithPagination(String mechanicUsername, int pageNo, int pageSize, String userRole, String requestingUsername, boolean includeOlderTasks) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending());
        Page<ScheduleTask> pagingScheduleTask;
        LocalDate currentDate = LocalDate.now();

        if (userRole.equals("ROLE_ADMIN") || userRole.equals("ROLE_PLANNER") || Objects.equals(requestingUsername, mechanicUsername)) {
            if (includeOlderTasks) {
                pagingScheduleTask = scheduleTaskRepository.findByMechanicUsername(mechanicUsername, pageRequest);
            } else {
                pagingScheduleTask = scheduleTaskRepository.findByMechanicUsernameAndDateAfter(mechanicUsername, currentDate, pageRequest);
            }
        } else {
            throw new BadRequestException("U mag deze gegevens niet inzien");
        }

        PageResponse<ScheduleTaskOutputDto> response = new PageResponse<>();

        response.count = pagingScheduleTask.getTotalElements();
        response.totalPages = pagingScheduleTask.getTotalPages();
        response.hasNext = pagingScheduleTask.hasNext();
        response.hasPrevious = pagingScheduleTask.hasPrevious();
        response.items = new ArrayList<>();

        for (ScheduleTask t : pagingScheduleTask) {
            response.items.add(transferScheduleTaskToOutputDto(t));
        }

        return response;
    }

    public ScheduleTaskOutputDto addScheduleTask(ScheduleTaskInputDto scheduleTaskInputDto) {
        ScheduleTask scheduleTask = transferDtoToScheduleTask(scheduleTaskInputDto);

        LocalDate date = scheduleTask.getDate();
        LocalTime startTime = scheduleTask.getStartTime();
        LocalTime endTime = scheduleTask.getEndTime();
        User mechanic = scheduleTask.getMechanic();

        if(endTime.compareTo(startTime) <= 0) {
            throw new BadRequestException("Eindtijd kan niet voor de begintijd liggen");
        }

        boolean isMechanicAlreadyScheduled = scheduleTaskRepository.countConflictingTasks(mechanic, date, startTime, endTime) > 0;

        if (isMechanicAlreadyScheduled) {
            throw new RelationFoundException("De monteur is al ingepland op deze dag en tijd.");
        }
        scheduleTaskRepository.save(scheduleTask);

        return transferScheduleTaskToOutputDto(scheduleTask);
    }

    public ScheduleTaskOutputDto updateScheduleTask(Long id, ScheduleTaskInputDto scheduleTaskInputDto) {
        //todo: controle toevoegen of tijden kloppen en niet overlappen bij monteurs en tijden (het id dat je wilt aanpassen uitsluiten! - optionele parameters? / Optional voor laatste param (standaard null? isPresent check))

        ScheduleTask updateScheduleTask = transferDtoToScheduleTask(scheduleTaskInputDto, id);
        updateScheduleTask.setId(id);
        scheduleTaskRepository.save(updateScheduleTask);

        return transferScheduleTaskToOutputDto(updateScheduleTask);
    }

    public ScheduleTaskOutputDto assignScheduleToTask(Long id, Long task_id) {
        // in plaats van een aparte optional aan te maken heb ik hier de optie gebruikt om meteen een exception te gooien als het ID niet bestaat
        ScheduleTask scheduleTask = scheduleTaskRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Geen planning gevonden met id: " + id));
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RecordNotFoundException("Geen taak gevonden met id: " + task_id));

        scheduleTask.setTask(task);
        scheduleTaskRepository.save(scheduleTask);

        return transferScheduleTaskToOutputDto(scheduleTask);
    }

    public void deleteScheduleTask(Long id) {
        Optional<ScheduleTask> optionalScheduleTask = scheduleTaskRepository.findById(id);
        if(optionalScheduleTask.isEmpty()) {
            throw new RecordNotFoundException("Geen ingeplande taak gevonden met id: " + id);
        }

        scheduleTaskRepository.deleteById(id);
    }

    public ScheduleTaskOutputDto transferScheduleTaskToOutputDto(ScheduleTask scheduleTask) {
        ScheduleTaskOutputDto scheduleTaskOutputDto = new ScheduleTaskOutputDto();

        scheduleTaskOutputDto.id = scheduleTask.getId();
        scheduleTaskOutputDto.date = scheduleTask.getDate();
        scheduleTaskOutputDto.startTime = scheduleTask.getStartTime();
        scheduleTaskOutputDto.endTime = scheduleTask.getEndTime();
        scheduleTaskOutputDto.task = scheduleTask.getTask();
        scheduleTaskOutputDto.mechanic = scheduleTask.getMechanic();

        return scheduleTaskOutputDto;
    }

    public ScheduleTask transferDtoToScheduleTask(ScheduleTaskInputDto scheduleTaskInputDto) {
        return transferDtoToScheduleTask(scheduleTaskInputDto, 0L);
    }

    public ScheduleTask transferDtoToScheduleTask(ScheduleTaskInputDto scheduleTaskInputDto, Long id) {
        ScheduleTask scheduleTask;

        if(id != 0L) {
            Optional<ScheduleTask> scheduleTaskOptional = scheduleTaskRepository.findById(id);
            if(scheduleTaskOptional.isEmpty()) {
                throw new RecordNotFoundException("Geen ingeplande taak gevonden met id: " + id);
            }
            scheduleTask = scheduleTaskOptional.get();
        } else {
            scheduleTask = new ScheduleTask();
        }

        // Geen setId nodig, deze genereert de database of staat in de URL
        if(scheduleTaskInputDto.date != null)
            scheduleTask.setDate(scheduleTaskInputDto.date);
        if(scheduleTaskInputDto.startTime != null)
            scheduleTask.setStartTime(scheduleTaskInputDto.startTime);
        if(scheduleTaskInputDto.endTime != null)
            scheduleTask.setEndTime(scheduleTaskInputDto.endTime);
        if(scheduleTaskInputDto.task != null)
            scheduleTask.setTask(scheduleTaskInputDto.task);
        if(scheduleTaskInputDto.mechanic != null)
            scheduleTask.setMechanic(scheduleTaskInputDto.mechanic);

        return scheduleTask;
    }
}

