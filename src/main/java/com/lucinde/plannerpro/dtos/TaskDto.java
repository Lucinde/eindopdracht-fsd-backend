package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.models.File;
import com.lucinde.plannerpro.models.ScheduleTask;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class TaskDto {
    public Long id;
    @NotBlank
    public String description;
    public String workPerformed;
    public Boolean jobDone;
    public Customer customer;
    @JsonIgnore
    public List<File> fileList;
    public List<ScheduleTask> scheduleTaskList;

    //todo: DTO's maken waarin de afbeeldingen niet meegegeven worden -> als ik hem als JSON ignore niet nodig?

    //todo: Alle delete services nalopen om te checken of onderdelen niet aan elkaar gekoppeld zijn voor ze verwijderd mogen worden
}
