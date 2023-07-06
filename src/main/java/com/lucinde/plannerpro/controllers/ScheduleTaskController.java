package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
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
    public ResponseEntity<List<ScheduleTaskDto>> getAllScheduleTasks() {
        return ResponseEntity.ok().body(scheduleTaskService.getAllScheduleTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleTaskDto> getScheduleTask(@PathVariable Long id) {
        return ResponseEntity.ok().body(scheduleTaskService.getScheduleTask(id));
    }

    @GetMapping("/pages")
    public ResponseEntity<Object> getTasksWithPagination(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam boolean includeOlderTasks) {
        PageResponse<ScheduleTaskDto> scheduleTaskDto = scheduleTaskService.getScheduleTaskWithPagination(pageNo, pageSize, includeOlderTasks);

        return ResponseEntity.ok().body(scheduleTaskDto);
    }

    @GetMapping("/pages/{mechanic}")
    public ResponseEntity<Object> getScheduleTasksByMechanicWithPagination(@PathVariable String mechanic, @RequestParam Integer pageNo, @RequestParam Integer pageSize, Authentication authentication, @RequestParam boolean includeOlderTasks) {
        String userRole = getUserRole(authentication);
        String requestingUsername = authentication.getName();

        PageResponse<ScheduleTaskDto> scheduleTaskDto = scheduleTaskService.getScheduleTasksByMechanicWithPagination(mechanic, pageNo, pageSize, userRole, requestingUsername, includeOlderTasks);

        return ResponseEntity.ok().body(scheduleTaskDto);
    }

    @PostMapping
    public ResponseEntity<Object> addScheduleTask(@Valid @RequestBody ScheduleTaskDto scheduleTaskDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
        }
        ScheduleTaskDto addedScheduleTask = scheduleTaskService.addScheduleTask(scheduleTaskDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedScheduleTask.id)));
        return ResponseEntity.created(uri).body(addedScheduleTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateScheduleTask(@PathVariable Long id, @Valid @RequestBody ScheduleTaskDto scheduleTaskDto, BindingResult br) {
        //todo: goed nadenken of deze check/@Valid nodig is. Bij het updaten van een taak, mag de datum dan wel naar het verleden verplaatst worden?
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
        }
        ScheduleTaskDto updateScheduleTask = scheduleTaskService.updateScheduleTask(id, scheduleTaskDto);
        return ResponseEntity.ok().body(updateScheduleTask);
    }

    @PutMapping("/{id}/task/{task_id}")
    public ResponseEntity<ScheduleTaskDto> assignScheduleToTask(@PathVariable Long id, @PathVariable Long task_id) {
        return ResponseEntity.ok().body(scheduleTaskService.assignScheduleToTask(id, task_id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteScheduleTask(@PathVariable Long id) {
        scheduleTaskService.deleteScheduleTask(id);
        return ResponseEntity.noContent().build();
    }

    private String getUserRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // Assuming the user has a single role, retrieve the first authority
        return authorities.iterator().next().getAuthority();
    }
}
