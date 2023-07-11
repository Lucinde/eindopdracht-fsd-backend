package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.ScheduleTaskInputDto;
import com.lucinde.plannerpro.dtos.ScheduleTaskOutputDto;
import com.lucinde.plannerpro.exceptions.BadRequestException;
import com.lucinde.plannerpro.utils.FieldError;
import com.lucinde.plannerpro.services.ScheduleTaskService;
import com.lucinde.plannerpro.utils.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/schedule-tasks")
public class ScheduleTaskController {
    private final ScheduleTaskService scheduleTaskService;
    private final FieldError fieldError = new FieldError();


    public ScheduleTaskController(ScheduleTaskService scheduleTaskService) {
        this.scheduleTaskService = scheduleTaskService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleTaskOutputDto>> getAllScheduleTasks() {
        return ResponseEntity.ok().body(scheduleTaskService.getAllScheduleTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleTaskOutputDto> getScheduleTask(@PathVariable Long id) {
        return ResponseEntity.ok().body(scheduleTaskService.getScheduleTask(id));
    }

    @GetMapping("/pages")
    public ResponseEntity<Object> getTasksWithPagination(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(defaultValue = "false") boolean includeOlderTasks) {

        if(pageNo == null) {
            throw new BadRequestException("Vul een pageNo in");
        }
        if(pageSize == null) {
            throw new BadRequestException("Vul een pageSize in");
        }

        PageResponse<ScheduleTaskOutputDto> scheduleTaskDto = scheduleTaskService.getScheduleTaskWithPagination(pageNo, pageSize, includeOlderTasks);

        return ResponseEntity.ok().body(scheduleTaskDto);
    }

    @GetMapping("/pages/{mechanic}")
    public ResponseEntity<Object> getScheduleTasksByMechanicWithPagination(
            @PathVariable String mechanic,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            Authentication authentication,
            @RequestParam(defaultValue = "false") boolean includeOlderTasks) {

        if(pageNo == null) {
            throw new BadRequestException("Vul een pageNo in");
        }
        if(pageSize == null) {
            throw new BadRequestException("Vul een pageSize in");
        }

        String userRole = getUserRole(authentication);
        String requestingUsername = authentication.getName();

        PageResponse<ScheduleTaskOutputDto> scheduleTaskDto = scheduleTaskService.getScheduleTasksByMechanicWithPagination(mechanic, pageNo, pageSize, userRole, requestingUsername, includeOlderTasks);

        return ResponseEntity.ok().body(scheduleTaskDto);
    }

    @PostMapping
    public ResponseEntity<Object> addScheduleTask(@Valid @RequestBody ScheduleTaskInputDto scheduleTaskInputDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
        }
        ScheduleTaskOutputDto addedScheduleTask = scheduleTaskService.addScheduleTask(scheduleTaskInputDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedScheduleTask.id)));
        return ResponseEntity.created(uri).body(addedScheduleTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateScheduleTask(@PathVariable Long id, @RequestBody ScheduleTaskInputDto scheduleTaskInputDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
        }
        ScheduleTaskOutputDto updateScheduleTask = scheduleTaskService.updateScheduleTask(id, scheduleTaskInputDto);
        return ResponseEntity.ok().body(updateScheduleTask);
    }

    @PutMapping("/{id}/task/{task_id}")
    public ResponseEntity<ScheduleTaskOutputDto> assignScheduleToTask(@PathVariable Long id, @PathVariable Long task_id) {
        return ResponseEntity.ok().body(scheduleTaskService.assignScheduleToTask(id, task_id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteScheduleTask(@PathVariable Long id) {
        scheduleTaskService.deleteScheduleTask(id);
        return ResponseEntity.noContent().build();
    }

    private String getUserRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.iterator().next().getAuthority();
    }
}
