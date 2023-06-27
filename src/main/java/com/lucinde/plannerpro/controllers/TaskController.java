package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.TaskDto;
import com.lucinde.plannerpro.utils.Helpers;
import com.lucinde.plannerpro.services.TaskService;
import com.lucinde.plannerpro.utils.PageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

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

    @GetMapping({"/pages"})
    public ResponseEntity<Object> getTasksWithPagination(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        PageResponse<TaskDto> taskDto = taskService.getTasksWithPagination(pageNo, pageSize);

        //todo: dit moet in React gedaan worden, als dat werkt kan dit verwijderd worden en de response aangepast
//        int nextPage = pageNo + 1;
//        String nextPageLink = null;
//        if (nextPage > 0 && taskDto != null && !taskDto.isEmpty()) {
//            UriComponentsBuilder builderNext = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/pages")
//                    .queryParam("pageNo", nextPage)
//                    .queryParam("pageSize", pageSize);
//            nextPageLink = builderNext.toUriString();
//        }
//
//        int prevPage = pageNo - 1;
//        String prevPageLink = null;
//        if (prevPage >= 0) {
//            UriComponentsBuilder builderPrev = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/pages")
//                    .queryParam("pageNo", prevPage)
//                    .queryParam("pageSize", pageSize);
//            prevPageLink = builderPrev.toUriString();
//        }

        return ResponseEntity.ok().body(taskDto);
//        return ResponseEntity.ok().headers(HeaderUtil.createLinkHeader(nextPageLink, prevPageLink)).body(taskDto);
    }

    @PostMapping
    public ResponseEntity<Object> addTask(@Valid @RequestBody TaskDto taskDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(helpers.fieldErrorBuilder(br));
        }
        TaskDto addedTask = taskService.addTask(taskDto);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedTask.id)));
        return ResponseEntity.created(uri).body(addedTask);
    }

    //todo: Add post/putmapping to add a customer to a task

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto, BindingResult br) {
        if (br.hasFieldErrors()) {
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

//    public static class HeaderUtil {
//        public static HttpHeaders createLinkHeader(String nextPageLink, String previousLink) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("next", nextPageLink);
//            headers.add("previous", previousLink);
//            return headers;
//        }
//    }

}
