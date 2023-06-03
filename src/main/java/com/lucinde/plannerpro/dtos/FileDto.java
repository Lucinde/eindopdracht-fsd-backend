package com.lucinde.plannerpro.dtos;

import com.lucinde.plannerpro.models.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class FileDto {
    public Long id;

    public String filename;
    @NotBlank
    public byte[] data;
    public String mimeType;
    public String description;
    public Task task;
}
