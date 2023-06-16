package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.TaskDto;
import com.lucinde.plannerpro.helpers.Helpers;
import com.lucinde.plannerpro.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final Helpers helpers = new Helpers();

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok().body(taskService.getAllTasks());
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok().body(taskService.getTask(id));
    }

    @PostMapping
    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskDto taskDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
        }
        TaskDto addedTask = taskService.addTask(taskDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedTask.id)));
        return ResponseEntity.created(uri).body(addedTask);
    }

    //todo: Add post/putmapping to add a customer to a task

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
        }
        TaskDto updateTask = taskService.updateTask(id, taskDto);
        return ResponseEntity.ok().body(updateTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
