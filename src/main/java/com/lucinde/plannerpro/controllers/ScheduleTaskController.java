package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.ScheduleTaskDto;
import com.lucinde.plannerpro.services.ScheduleTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule-task")
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
}
