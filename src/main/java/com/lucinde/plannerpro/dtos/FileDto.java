package com.lucinde.plannerpro.dtos;

import com.lucinde.plannerpro.models.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FileDto {
    public Long id;

    public String filename;
    @NotBlank
    public byte[] data;
    public String mimeType;
    @NotBlank(message = "fill in description")
    public String description;
    public Task task;
}
