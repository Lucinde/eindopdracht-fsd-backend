package com.lucinde.plannerpro.dtos;

import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.models.File;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class TaskDto {
    public Long id;
    @NotBlank
    public String description;
    public String workPerformed;
    public Boolean jobDone;
    public Customer customer;
    public List<File> fileList;

}
