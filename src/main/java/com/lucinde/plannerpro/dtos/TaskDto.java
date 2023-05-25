package com.lucinde.plannerpro.dtos;

import jakarta.validation.constraints.NotBlank;

public class TaskDto {
    public Long id;
    @NotBlank
    public String description;
    public String workPerformed;
    public Boolean jobDone;
}
