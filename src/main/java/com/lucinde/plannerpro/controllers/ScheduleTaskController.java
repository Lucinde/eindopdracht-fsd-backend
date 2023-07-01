package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
import com.lucinde.plannerpro.utils.Helpers;
import com.lucinde.plannerpro.services.ScheduleTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/schedule-tasks")
public class ScheduleTaskController {
    private final ScheduleTaskService scheduleTaskService;
    private final Helpers helpers = new Helpers();


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

    @PostMapping
    public ResponseEntity<Object> addScheduleTask(@Valid @RequestBody ScheduleTaskDto scheduleTaskDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
        }
        ScheduleTaskDto addedScheduleTask = scheduleTaskService.addScheduleTask(scheduleTaskDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedScheduleTask.id)));
        return ResponseEntity.created(uri).body(addedScheduleTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateScheduleTask(@PathVariable Long id, @Valid @RequestBody ScheduleTaskDto scheduleTaskDto, BindingResult br) {
        //todo: goed nadenken of deze check/@Valid nodig is. Bij het updaten van een taak, mag de datum dan wel naar het verleden verplaatst worden?
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
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
}
