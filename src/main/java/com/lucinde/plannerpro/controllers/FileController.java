package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.FileDto;
import com.lucinde.plannerpro.utils.FieldError;
import com.lucinde.plannerpro.services.FileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final FieldError fieldError = new FieldError();

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getAllFiles() {
        return ResponseEntity.ok().body(fileService.getAllFiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFile(@PathVariable Long id) {
        return ResponseEntity.ok().body(fileService.getFile(id));
    }

    @GetMapping("/task/{task_id}")
    public ResponseEntity<List<FileDto>> getFilesByTaskId(@PathVariable Long task_id) {
        return ResponseEntity.ok().body(fileService.getFilesByTaskId(task_id));
    }

    @PostMapping
    public ResponseEntity<Object> addFile(@RequestParam()MultipartFile file, @Valid @RequestParam() String description, @RequestParam() Long task_id) throws IOException, HttpClientErrorException.BadRequest {
        if(file.isEmpty()) {
            throw new FileNotFoundException("Je moet een bestand uploaden!");
        }

        FileDto addedFile = fileService.addFile(file, description, task_id);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedFile.id)));
        return ResponseEntity.created(uri).body(addedFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFile(@PathVariable Long id, @RequestParam() MultipartFile file, @RequestParam() String description, @RequestParam() Long task_id) throws IOException {
        if(file.isEmpty()) {
            throw new FileNotFoundException("Je moet een bestand uploaden!");
        }

        FileDto updateFile = fileService.updateFile(id, file, description, task_id);
        return ResponseEntity.ok().body(updateFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }


}
