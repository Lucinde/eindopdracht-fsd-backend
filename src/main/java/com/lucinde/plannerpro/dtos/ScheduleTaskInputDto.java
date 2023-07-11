package com.lucinde.plannerpro.dtos;

import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.models.User;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTaskInputDto {
    public Long id;
    @FutureOrPresent
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    public Task task;
    public User mechanic;

}
