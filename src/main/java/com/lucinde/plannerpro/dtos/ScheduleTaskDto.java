package com.lucinde.plannerpro.dtos;

import com.lucinde.plannerpro.models.Task;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleTaskDto {
    public Long id;
    @FutureOrPresent
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    @NotEmpty
    public Task task;
}
