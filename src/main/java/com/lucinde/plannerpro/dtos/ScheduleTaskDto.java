package com.lucinde.plannerpro.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTaskDto {
    public Long id;
    @NotEmpty
    public LocalDate date;
    @NotEmpty
    public LocalTime startTime;
    @NotEmpty
    public LocalTime endTime;
}
