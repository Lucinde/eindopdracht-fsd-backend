package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.models.File;
import com.lucinde.plannerpro.models.ScheduleTask;
import jakarta.transaction.Transactional;
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

}
