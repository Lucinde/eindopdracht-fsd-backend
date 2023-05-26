package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.FileDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.File;
import com.lucinde.plannerpro.repositories.FileRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileDto> getAllFiles() {
        Iterable<File> files = fileRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<FileDto> fileDtos = new ArrayList<>();

        for (File f: files) {
            fileDtos.add(transferFileToDto(f));
        }
        return fileDtos;
    }

    public FileDto getFile(Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);

        if(fileOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen bestand gevonden met id: " + id);
        }

        File file = fileOptional.get();

        return transferFileToDto(file);
    }

    public FileDto addFile(MultipartFile fileUpload, String description) throws IOException {
        File newFile = createNewFile(fileUpload, description);
        fileRepository.save(newFile);
        return transferFileToDto(newFile);
    }

    public FileDto updateFile(Long id, MultipartFile fileUpload, String description) throws IOException {
        Optional<File> fileOptional = fileRepository.findById(id);
        if(fileOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen bestand gevonden met id: " + id);
        }

        File newFile = createNewFile(fileUpload, description);
        newFile.setId(id);
        fileRepository.save(newFile);

        return transferFileToDto(newFile);
    }

    public void deleteFile(Long id) {
        Optional<File> optionalFile = fileRepository.findById(id);
        if(optionalFile.isEmpty()) {
            throw new RecordNotFoundException("Geen bestand gevonden met id: " + id);
        }
        fileRepository.deleteById(id);
    }

    private File createNewFile(MultipartFile fileUpload, String description) throws IOException {
        File newFile = new File();
        newFile.setData(fileUpload.getBytes());
        newFile.setMimeType(fileUpload.getContentType());
        newFile.setFilename(fileUpload.getOriginalFilename());
        newFile.setDescription(description);

        return newFile;
    }

    public FileDto transferFileToDto(File file) {
        FileDto fileDto = new FileDto();

        fileDto.id = file.getId();
        fileDto.filename = file.getFilename();
        fileDto.description = file.getDescription();
        fileDto.data = file.getData();
        fileDto.mimeType = file.getMimeType();

        return fileDto;
    }

    public File transferDtoToFile(FileDto fileDto) {
        File file = new File();

        // Geen setId nodig, deze genereert de database of staat in de URL
        file.setFilename(fileDto.filename);
        file.setDescription(fileDto.description);
        file.setData(fileDto.data);
        file.setMimeType(fileDto.mimeType);

        return file;
    }
}
