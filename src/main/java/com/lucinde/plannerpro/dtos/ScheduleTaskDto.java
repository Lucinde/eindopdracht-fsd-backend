package com.lucinde.plannerpro.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTaskDto {
    public Long id;
    @NotEmpty
    @FutureOrPresent
    public LocalDate date;
    @NotEmpty
    public LocalTime startTime;
    @NotEmpty
    public LocalTime endTime;
}
