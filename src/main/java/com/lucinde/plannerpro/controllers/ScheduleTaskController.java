package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
import com.lucinde.plannerpro.services.ScheduleTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/schedule-tasks")
public class ScheduleTaskController {
    private final ScheduleTaskService scheduleTaskService;


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
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField() + ": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            ScheduleTaskDto addedScheduleTask = scheduleTaskService.addScheduleTask(scheduleTaskDto);
            URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedScheduleTask.id)));
            return ResponseEntity.created(uri).body(addedScheduleTask);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateScheduleTask(@PathVariable Long id, @RequestBody ScheduleTaskDto scheduleTaskDto) {
        ScheduleTaskDto updateScheduleTask = scheduleTaskService.updateScheduleTask(id, scheduleTaskDto);
        return ResponseEntity.ok().body(updateScheduleTask);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteScheduleTask(@PathVariable Long id) {
        scheduleTaskService.deleteScheduleTask(id);
        return ResponseEntity.noContent().build();
    }
}
