package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
import com.lucinde.plannerpro.dtos.TaskDto;
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

    public PageResponse<ScheduleTaskDto> getScheduleTaskWithPagination(int pageNo, int pageSize, boolean includeOlderTasks) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending());
        Page<ScheduleTask> pagingScheduleTask;
        LocalDate currentDate = LocalDate.now();

        if(includeOlderTasks) {
            pagingScheduleTask = scheduleTaskRepository.findAll(pageRequest);
        } else {
            pagingScheduleTask = scheduleTaskRepository.findAllByDateAfter(currentDate, pageRequest);
        }

        PageResponse<ScheduleTaskDto> response = new PageResponse<>();

        response.count = pagingScheduleTask.getTotalElements();
        response.totalPages = pagingScheduleTask.getTotalPages();
        response.hasNext = pagingScheduleTask.hasNext();
        response.hasPrevious = pagingScheduleTask.hasPrevious();
        response.items = new ArrayList<>();

        for (ScheduleTask t : pagingScheduleTask) {
            response.items.add(transferScheduleTaskToDto(t));
        }

        return response;
    }

    public PageResponse<ScheduleTaskDto> getScheduleTasksByMechanicWithPagination(String mechanicUsername, int pageNo, int pageSize, String userRole, String requestingUsername, boolean includeOlderTasks) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending());
        Page<ScheduleTask> pagingScheduleTask;

        if (userRole.equals("ROLE_ADMIN") || userRole.equals("ROLE_PLANNER")) {
            // Admin or planner can see all tasks
            pagingScheduleTask = scheduleTaskRepository.findByMechanicUsername(mechanicUsername, pageRequest);
        } else {
            // Mechanic can only see their own tasks
            if(Objects.equals(requestingUsername, mechanicUsername)) {
                pagingScheduleTask = scheduleTaskRepository.findByMechanicUsername(mechanicUsername, pageRequest);
            } else {
                throw new BadRequestException("U mag deze gegevens niet inzien");
            }
        }

        PageResponse<ScheduleTaskDto> response = new PageResponse<>();

        response.count = pagingScheduleTask.getTotalElements();
        response.totalPages = pagingScheduleTask.getTotalPages();
        response.hasNext = pagingScheduleTask.hasNext();
        response.hasPrevious = pagingScheduleTask.hasPrevious();
        response.items = new ArrayList<>();

        // Nodig wanneer we alleen taken in het heden of de toekomst willen laten zien
        LocalDate currentDate = LocalDate.now();

        for (ScheduleTask t : pagingScheduleTask) {
            if (includeOlderTasks || t.getDate().isAfter(currentDate) || t.getDate().isEqual(currentDate)) {
                response.items.add(transferScheduleTaskToDto(t));
            }
        }

        return response;
    }

    public ScheduleTaskDto addScheduleTask(ScheduleTaskDto scheduleTaskDto) {
        ScheduleTask scheduleTask = transferDtoToScheduleTask(scheduleTaskDto);

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

        return transferScheduleTaskToDto(scheduleTask);
    }

    public ScheduleTaskDto updateScheduleTask(Long id, ScheduleTaskDto scheduleTaskDto) {
        //todo: controle toevoegen of tijden kloppen en niet overlappen bij monteurs en tijden (het id dat je wilt aanpassen uitsluiten! - optionele parameters? / Optional voor laatste param (standaard null? isPresent check))

        ScheduleTask updateScheduleTask = transferDtoToScheduleTask(scheduleTaskDto, id);
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
        scheduleTaskDto.mechanic = scheduleTask.getMechanic();

        return scheduleTaskDto;
    }

    public ScheduleTask transferDtoToScheduleTask(ScheduleTaskDto scheduleTaskDto) {
        return transferDtoToScheduleTask(scheduleTaskDto, 0L);
    }

    public ScheduleTask transferDtoToScheduleTask(ScheduleTaskDto scheduleTaskDto, Long id) {
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
        if(scheduleTaskDto.date != null)
            scheduleTask.setDate(scheduleTaskDto.date);
        if(scheduleTaskDto.startTime != null)
            scheduleTask.setStartTime(scheduleTaskDto.startTime);
        if(scheduleTaskDto.endTime != null)
            scheduleTask.setEndTime(scheduleTaskDto.endTime);
        if(scheduleTaskDto.task != null)
            scheduleTask.setTask(scheduleTaskDto.task);
        if(scheduleTaskDto.mechanic != null)
            scheduleTask.setMechanic(scheduleTaskDto.mechanic);

        return scheduleTask;
    }
}

