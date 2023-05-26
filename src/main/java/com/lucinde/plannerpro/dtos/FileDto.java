package com.lucinde.plannerpro.dtos;

import jakarta.validation.constraints.NotBlank;

public class FileDto {
    public Long id;

    public String filename;
    @NotBlank
    public String pathToImage;
    public String description;
}
