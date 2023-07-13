package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.TaskDto;
import com.lucinde.plannerpro.exceptions.BadRequestException;
import com.lucinde.plannerpro.utils.FieldError;
import com.lucinde.plannerpro.services.TaskService;
import com.lucinde.plannerpro.utils.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final FieldError fieldError = new FieldError();

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

    @GetMapping({"/pages"})
    public ResponseEntity<Object> getTasksWithPagination(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize) {

        if (pageNo == null) {
            throw new BadRequestException("Vul een pageNo in");
        }
        if (pageSize == null) {
            throw new BadRequestException("Vul een pageSize in");
        }

        PageResponse<TaskDto> taskDto = taskService.getTasksWithPagination(pageNo, pageSize);

        return ResponseEntity.ok().body(taskDto);
    }

    @PostMapping
    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskDto taskDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
        }
        TaskDto addedTask = taskService.addTask(taskDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedTask.id)));
        return ResponseEntity.created(uri).body(addedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskDto taskDto,
            BindingResult br) {
        if (br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
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
