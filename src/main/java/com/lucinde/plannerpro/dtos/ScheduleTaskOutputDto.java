package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.models.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleTaskOutputDto {
    public Long id;
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    public Task task;

    @JsonIgnore
    public User mechanic;

    @JsonProperty("mechanic")
    public String getMechanicUsername() {
        return mechanic.getUsername();
    }
}
