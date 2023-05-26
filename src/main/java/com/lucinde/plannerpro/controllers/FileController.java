package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.FileDto;
import com.lucinde.plannerpro.services.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;


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

    @PostMapping
    public ResponseEntity<Object> addFile(@RequestParam("file")MultipartFile file, @RequestParam("description") String description) throws IOException {
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("Je moet een bestand uploaden!");
        }
        FileDto addedFile = fileService.addFile(file, description);
        URI uri = URI.create(String.valueOf(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + addedFile.id)));
        return ResponseEntity.created(uri).body(addedFile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam("description") String description) throws IOException {
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("Je moet een bestand uploaden!");
        }
        FileDto updateFile = fileService.updateFile(id, file, description);
        return ResponseEntity.ok().body(updateFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }


}
